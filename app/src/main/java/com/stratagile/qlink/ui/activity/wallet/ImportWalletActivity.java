package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerImportWalletComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.ImportWalletContract;
import com.stratagile.qlink.ui.activity.wallet.module.ImportWalletModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.ImportWalletPresenter;
import com.stratagile.qlink.utils.StringUitl;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.TextSpaceView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/23 14:24:49
 */

public class ImportWalletActivity extends BaseActivity implements ImportWalletContract.View {

    @Inject
    ImportWalletPresenter mPresenter;
    @BindView(R.id.et_private_key)
    EditText etPrivateKey;
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.bt_import)
    Button btImport;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.importWalletParent)
    LinearLayout importWalletParent;
    private String fromType;//跳转来源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_import_wallet);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        fromType = getIntent().getStringExtra("fromType");
        tvTitle.setText(R.string.Import_My_Wallet);
        importWalletParent.setBackgroundResource(R.drawable.navigation_shape);
        if(fromType != null)
        {
            switch (fromType)
            {
                case "worldCup"://世界杯背景
                    importWalletParent.setBackgroundResource(R.mipmap.bg_world_cup_two);
                    break;
                default:
                    importWalletParent.setBackgroundResource(R.drawable.navigation_shape);
                    break;
            }
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerImportWalletComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .importWalletModule(new ImportWalletModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ImportWalletContract.ImportWalletContractPresenter presenter) {
        mPresenter = (ImportWalletPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @Override
    public void onImportWalletSuccess(CreateWallet createWallet) {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        for (Wallet wallet : walletList) {
            if (wallet.getPrivateKey().equals(createWallet.getData().getPrivateKey())) {
                ToastUtil.displayShortToast(getString(R.string.this_wallet_is_exist));
                closeProgressDialog();
                finish();
                return;
            }
        }
        closeProgressDialog();
        Intent intent = new Intent(this, WalletCreatedActivity.class);
        intent.putExtra("fromType", fromType);
        intent.putExtra("wallet", createWallet.getData());
        intent.putExtra("title", "wallet imported");
        startActivity(intent);
        AppConfig.getInstance().getDaoSession().getWalletDao().insert(createWallet.getData());
        setResult(RESULT_OK);
        finish();
    }

    @OnClick({R.id.bt_back, R.id.bt_import})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_import:
                String  privateKey = etPrivateKey.getText().toString().replace(" ", "");
                if (privateKey.equals("")) {
                    ToastUtil.displayShortToast(getResources().getString(R.string.privatekeyisnone));
                    return;
                }
                if (privateKey.length() >= 42) {
                    Intent intent = new Intent();
                    boolean isSupperCase = StringUitl.isBase64(privateKey);
                    if(isSupperCase)
                    {
                        byte[] bytesPrivateKey = Base64.decode(privateKey, Base64.NO_WRAP);
                        byte[] bytes = qlinkcom.AES(bytesPrivateKey,1);
                        String decryptPrivateKey = new String(bytes);
                        intent.putExtra("result", decryptPrivateKey);
                    }else{
                        intent.putExtra("result", privateKey);
                    }

                    setResult(RESULT_OK, intent);
                    onBackPressed();
                } else {
                    ToastUtil.displayShortToast(getResources().getString(R.string.privatekeyerror));
                }
                break;
            default:
                break;
        }
    }
}