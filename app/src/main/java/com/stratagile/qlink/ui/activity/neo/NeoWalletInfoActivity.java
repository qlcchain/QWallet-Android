package com.stratagile.qlink.ui.activity.neo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.QrEntity;
import com.stratagile.qlink.ui.activity.wallet.WalletQRCodeActivity;
import com.stratagile.qlink.ui.activity.neo.component.DaggerNeoWalletInfoComponent;
import com.stratagile.qlink.ui.activity.neo.contract.NeoWalletInfoContract;
import com.stratagile.qlink.ui.activity.neo.module.NeoWalletInfoModule;
import com.stratagile.qlink.ui.activity.neo.presenter.NeoWalletInfoPresenter;
import com.stratagile.qlink.view.SmoothCheckBox;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/11/05 17:19:51
 */

public class NeoWalletInfoActivity extends BaseActivity implements NeoWalletInfoContract.View {

    @Inject
    NeoWalletInfoPresenter mPresenter;
    @BindView(R.id.tvPublicAddress)
    TextView tvPublicAddress;
    @BindView(R.id.tvWif)
    TextView tvWif;
    @BindView(R.id.tvHex)
    TextView tvHex;
    @BindView(R.id.checkBox)
    SmoothCheckBox checkBox;
    @BindView(R.id.tvSend)
    TextView tvSend;

    private Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_neo_wallet_info);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle("NEO Wallet");
        wallet = getIntent().getParcelableExtra("wallet");
        tvHex.setText(wallet.getPrivateKey());
        tvWif.setText(wallet.getWif());
        tvPublicAddress.setText(wallet.getAddress());
    }

    @Override
    protected void setupActivityComponent() {
        DaggerNeoWalletInfoComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .neoWalletInfoModule(new NeoWalletInfoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(NeoWalletInfoContract.NeoWalletInfoContractPresenter presenter) {
        mPresenter = (NeoWalletInfoPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.tvWif, R.id.tvHex, R.id.tvSend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvWif:
                QrEntity qrEntity = new QrEntity(tvWif.getText().toString(), "Encrypted Key(WIF)", "");
                startActivity(new Intent(this, WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                break;
            case R.id.tvHex:
                QrEntity qrEntity1 = new QrEntity(tvHex.getText().toString(), "Private Key(HEX)", "");
                startActivity(new Intent(this, WalletQRCodeActivity.class).putExtra("qrentity", qrEntity1));
                break;
            case R.id.tvSend:
                if (checkBox.isChecked()) {
                    wallet.setIsBackup(true);
                    AppConfig.getInstance().getDaoSession().getWalletDao().update(wallet);
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}