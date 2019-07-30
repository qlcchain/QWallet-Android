package com.stratagile.qlink.ui.activity.wallet;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.eventbus.NeoRefrash;
import com.stratagile.qlink.guideview.Guide;
import com.stratagile.qlink.guideview.GuideBuilder;
import com.stratagile.qlink.guideview.GuideConstantValue;
import com.stratagile.qlink.guideview.GuideSpUtil;
import com.stratagile.qlink.guideview.compnonet.SaveWalletComponent;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerWalletDetailComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletDetailContract;
import com.stratagile.qlink.ui.activity.wallet.module.WalletDetailModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.WalletDetailPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.StringUitl;
import com.stratagile.qlink.utils.ToastUtil;
import com.vondear.rxtools.view.RxQRCode;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/19 10:21:00
 */

public class WalletDetailActivity extends BaseActivity implements WalletDetailContract.View {

    @Inject
    WalletDetailPresenter mPresenter;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.privatekey)
    TextView privatekey;
    @BindView(R.id.wif)
    TextView wif;
    @BindView(R.id.iv_address)
    ImageView ivAddress;
    @BindView(R.id.bt_export)
    Button btExport;
    @BindView(R.id.bt_change_wallet)
    Button btChangeWallet;
    Bitmap bitmap;
    @BindView(R.id.bt_copy_address)
    TextView btCopyAddress;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.iv_qr_1)
    ImageView ivQr1;
    @BindView(R.id.iv_qr_2)
    ImageView ivQr2;
    @BindView(R.id.tv_wif)
    TextView tvWif;
    @BindView(R.id.ll_wif)
    LinearLayout llWif;
    @BindView(R.id.tv_hex)
    TextView tvHex;
    @BindView(R.id.ll_hex)
    LinearLayout llHex;
    private Wallet wallet;

    private String fromType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_wallet_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.wallet_detail);
    }

    @Override
    protected void initData() {
        fromType = getIntent().getStringExtra("fromType");
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().queryBuilder().list();
        if (walletList != null && walletList.size() != 0) {
            wallet = walletList.get(SpUtil.getInt(this, ConstantValue.currentWallet, 0));
            address.setText(wallet.getAddress());
            wif.setText(wallet.getWif());
            privatekey.setText(wallet.getPrivateKey());
            bitmap = RxQRCode.builder(wallet.getAddress()).
                    backColor(getResources().getColor(com.vondear.rxtools.R.color.white)).
                    codeColor(getResources().getColor(com.vondear.rxtools.R.color.black)).
                    codeSide(800).
                    into(ivAddress);
        }
        cardView.post(new Runnable() {
            @Override
            public void run() {
                showGuideSaveWallet();
            }
        });
    }

    private void showGuideSaveWallet() {
        if (!GuideSpUtil.getBoolean(this, GuideConstantValue.isShowWalletDetailGuide, false)) {
            GuideSpUtil.putBoolean(this, GuideConstantValue.isShowWalletDetailGuide, true);
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(cardView)
                    .setAlpha(150)
                    .setHighTargetCorner(15)
                    .setHighTargetPaddingLeft(0)
                    .setHighTargetPaddingTop(0)
                    .setHighTargetPaddingBottom(0)
                    .setHighTargetPaddingRight(0)
                    .setOverlayTarget(false)
                    .setOutsideTouchable(true);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                }

                @Override
                public void onDismiss() {

                }
            });

            builder.addComponent(new SaveWalletComponent());
            Guide guide = builder.createGuide();
            guide.setShouldCheckLocInWindow(false);
            guide.show(this);
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerWalletDetailComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .walletDetailModule(new WalletDetailModule(this))
                .build()
                .inject(this);
    }

    private void showSecurutyCheckDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        btn_cancel.setText(R.string.cancel);
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        btn_comfirm.setText(R.string.continue2);
        tvContent.setText(Html.fromHtml(getResources().getString(R.string.private_key_qr_code)));
        title.setText(getString(R.string.security_check).toUpperCase());
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(WalletDetailActivity.this, QrCodeDetailActivity.class);
                if (position == 0) {
                    intent.putExtra("content", wallet.getWif());
                    intent.putExtra("title", "encrypted key");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(WalletDetailActivity.this, Pair.create(ivQr1, "qrcode"), Pair.create(cardView, "card"), Pair.create(tvWif, "title")).toBundle());
                    } else {
                        startActivity(intent);
                    }
                } else {
                    intent.putExtra("content", wallet.getPrivateKey());
                    intent.putExtra("title", "private key");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(WalletDetailActivity.this, Pair.create(ivQr2, "qrcode"), Pair.create(cardView, "card"), Pair.create(tvHex, "title")).toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            }
        });
        dialog.show();
    }

    @Override
    public void setPresenter(WalletDetailContract.WalletDetailContractPresenter presenter) {
        mPresenter = (WalletDetailPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.iv_address, R.id.bt_export, R.id.bt_change_wallet, R.id.bt_copy_address, R.id.ll_wif, R.id.ll_hex})
    public void onViewClicked(View view) {
        Intent intent = new Intent(this, QrCodeDetailActivity.class);
        switch (view.getId()) {
            case R.id.iv_address:
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                byte[] bitmapByte = baos.toByteArray();
                intent.putExtra("content", wallet.getAddress());
                intent.putExtra("title", "public address");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, Pair.create(view, "qrcode"), Pair.create(cardView, "card")).toBundle());
                } else {
                    startActivity(intent);
                }
                break;
            case R.id.bt_export:
                showEnterNameDialog();
                break;
            case R.id.bt_change_wallet:
                Intent intent1 = new Intent(this, ChangeWalletActivity.class);
                intent1.putExtra("fromType", fromType);
                startActivityForResult(intent1, 0);
                break;
            case R.id.bt_copy_address:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", address.getText().toString().trim());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.displayShortToast(getString(R.string.copy_success));
                break;
            case R.id.ll_wif:
                showSecurutyCheckDialog(0);
                break;
            case R.id.ll_hex:
                showSecurutyCheckDialog(1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            EventBus.getDefault().post(new NeoRefrash());
            initData();
            setResult(RESULT_OK);
        }
    }

    /**
     * 显示输入导出文件的名字的弹窗
     */
    private void showEnterNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.input_export_key_name_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);
        EditText etContent = view.findViewById(R.id.et_content);
        etContent.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(btExport.getWindowToken(), 0);
        Button btnComfirm = (Button) view.findViewById(R.id.btn_right);
        Button btnCancel = (Button) view.findViewById(R.id.btn_left);
        title.setText(getString(R.string.enter_a_filename).toUpperCase());
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btnComfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etContent.getText().toString().equals("")) {

                } else {
                    exportKeys(etContent.getText().toString());
                    dialog.dismiss();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void exportKeys(String name) {
        showProgressDialog();
        KLog.i("写入的内容为:" + privatekey.getText().toString());
        File walletFile = new File(Environment.getExternalStorageDirectory() + "/Qwallet/Address/" + name + ".txt", "");
        if (!walletFile.exists()) {
            try {
                walletFile.createNewFile();
                try {
                    FileOutputStream fos = new FileOutputStream(walletFile);
                    OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                    boolean isSupperCase = StringUitl.isBase64(privatekey.getText().toString());
                    if (isSupperCase) {
                        osw.write(privatekey.getText().toString());
                    } else {
                        byte[] bytes = qlinkcom.AES(privatekey.getText().toString().getBytes(), 0);
                        String encryptPrivateKey = Base64.encodeToString(bytes, Base64.NO_WRAP);
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
                osw.write(privatekey.getText().toString());
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

    private void showExportSuccess(File file) {
        KLog.i(file.getPath());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(getString(R.string.Your_private_keys_were_exported_to) + " \n" + file.getPath());
        title.setText(getString(R.string.export_successful).toUpperCase());
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Qwallet/Address/");
                intent.setDataAndType(uri, "*/*");
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