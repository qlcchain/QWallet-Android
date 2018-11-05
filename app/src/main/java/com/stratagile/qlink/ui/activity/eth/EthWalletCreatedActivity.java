package com.stratagile.qlink.ui.activity.eth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthWalletCreatedComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthWalletCreatedContract;
import com.stratagile.qlink.ui.activity.eth.module.EthWalletCreatedModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthWalletCreatedPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/10/23 15:15:28
 */

public class EthWalletCreatedActivity extends BaseActivity implements EthWalletCreatedContract.View {

    @Inject
    EthWalletCreatedPresenter mPresenter;
    @BindView(R.id.ivEth)
    ImageView ivEth;
    @BindView(R.id.tvEth)
    TextView tvEth;
    @BindView(R.id.btBackup)
    Button btBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_eth_wallet_created);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        ivEth.setSelected(true);
        tvEth.setSelected(true);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEthWalletCreatedComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .ethWalletCreatedModule(new EthWalletCreatedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EthWalletCreatedContract.EthWalletCreatedContractPresenter presenter) {
        mPresenter = (EthWalletCreatedPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.btBackup)
    public void onViewClicked() {
        startActivity(new Intent(this, EthMnemonicShowActivity.class).putExtra("wallet", (EthWallet)getIntent().getParcelableExtra("wallet")));
        finish();
    }
}