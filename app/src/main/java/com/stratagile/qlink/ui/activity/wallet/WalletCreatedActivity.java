package com.stratagile.qlink.ui.activity.wallet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.eventbus.AssetRefrash;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.main.MainActivity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerWalletCreatedComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletCreatedContract;
import com.stratagile.qlink.ui.activity.wallet.module.WalletCreatedModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.WalletCreatedPresenter;
import com.stratagile.qlink.utils.StringUitl;
import com.vondear.rxtools.RxFileTool;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/24 16:27:17
 */

public class WalletCreatedActivity extends BaseActivity implements WalletCreatedContract.View {

    @Inject
    WalletCreatedPresenter mPresenter;
    @BindView(R.id.publicAddress)
    TextView publicAddress;
    @BindView(R.id.encryptedKey)
    TextView encryptedKey;
    @BindView(R.id.privateKey)
    TextView privateKey;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.bt_export)
    Button btExport;
    @BindView(R.id.bt_continue)
    Button btContinue;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.walletCreatedParent)
    LinearLayout walletCreatedParent;
    private Wallet wallet;
    private String fromType;//跳转来源


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_wallet_created);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        wallet = getIntent().getParcelableExtra("wallet");
        fromType = getIntent().getStringExtra("fromType");
        KLog.i(wallet.toString());
        publicAddress.setText(wallet.getAddress());
        encryptedKey.setText(wallet.getWif());
        privateKey.setText(wallet.getPrivateKey());
        walletCreatedParent.setBackgroundResource(R.drawable.navigation_shape);
        tvTitle.setText(getIntent().getStringExtra("title").toUpperCase());
        autoExportKeys();
        if(fromType != null)
        {
            switch (fromType)
            {
                case "worldCup"://世界杯背景
                    walletCreatedParent.setBackgroundResource(R.mipmap.bg_world_cup_two);
                    break;
                default:
                    walletCreatedParent.setBackgroundResource(R.drawable.navigation_shape);
                    break;
            }
        }

    }

    @Override
    protected void setupActivityComponent() {
        DaggerWalletCreatedComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .walletCreatedModule(new WalletCreatedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(WalletCreatedContract.WalletCreatedContractPresenter presenter) {
        mPresenter = (WalletCreatedPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.bt_export, R.id.bt_continue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_export:
                exportKeys();
                break;
            case R.id.bt_continue:
                EventBus.getDefault().post(new AssetRefrash());
                finish();
                break;
            default:
                break;
        }
    }

    private void exportKeys() {
        showProgressDialog();
        KLog.i("写入的内容为:" + wallet.getPrivateKey());
        File walletFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/Address/qlinkdata.txt", "");
        if (!walletFile.exists()) {
            try {
                walletFile.createNewFile();
                try {
                    FileOutputStream fos = new FileOutputStream(walletFile);
                    OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");

                    boolean isSupperCase = StringUitl.isBase64(wallet.getPrivateKey());
                    if(isSupperCase)
                    {
                        osw.write(wallet.getPrivateKey());
                    }else{
                        byte[] bytes = qlinkcom.AES(wallet.getPrivateKey().getBytes(),0);
                        String encryptPrivateKey =  Base64.encodeToString(bytes,Base64.NO_WRAP);
                        osw.write(encryptPrivateKey);
                    }
                    osw.flush();
                    fos.flush();
                    osw.close();
                    fos.close();
                    closeProgressDialog();
                    showExportSuccess(walletFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                closeProgressDialog();
                e.printStackTrace();
            }
        } else {
            try {
                FileOutputStream fos = new FileOutputStream(walletFile);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                osw.write(wallet.getPrivateKey());
                osw.flush();
                fos.flush();
                osw.close();
                fos.close();
                closeProgressDialog();
                showExportSuccess(walletFile);
            } catch (Exception e) {
                closeProgressDialog();
                e.printStackTrace();
            }
        }
    }
    private void autoExportKeys() {
        KLog.i("写入的内容为:" + wallet.getPrivateKey());
        String walletName = publicAddress.getText().toString().trim();
        File walletFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/Address/"+walletName+".txt", "");
        if (!walletFile.exists()) {
            try {
                walletFile.createNewFile();
                try {
                    FileOutputStream fos = new FileOutputStream(walletFile);
                    OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                    boolean isSupperCase = StringUitl.isBase64(wallet.getPrivateKey());
                    if(isSupperCase)
                    {
                        osw.write(wallet.getPrivateKey());
                    }else{
                        byte[] bytes = qlinkcom.AES(wallet.getPrivateKey().getBytes(),0);
                        String encryptPrivateKey =  Base64.encodeToString(bytes,Base64.NO_WRAP);
                        osw.write(encryptPrivateKey);
                    }
                    osw.flush();
                    fos.flush();
                    osw.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileOutputStream fos = new FileOutputStream(walletFile);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                osw.write(wallet.getPrivateKey());
                osw.flush();
                fos.flush();
                osw.close();
                fos.close();
            } catch (Exception e) {
                closeProgressDialog();
                e.printStackTrace();
            }
        }
    }
    private void showExportSuccess(File file) {
        message.setVisibility(View.VISIBLE);
        KLog.i(file.getPath());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(getString(R.string.Your_private_keys_were_exported_to)+" \n" + file.getPath());
        title.setText(getString(R.string.export_successful).toUpperCase());
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri fileUri = RxFileTool.getUriForFile(AppConfig.getInstance(), file.getParentFile());
                intent.setDataAndType(fileUri, "*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivity(intent);
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}