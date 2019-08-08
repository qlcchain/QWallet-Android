package com.stratagile.qlink.ui.activity.neo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.ui.activity.neo.component.DaggerWalletCreatedComponent;
import com.stratagile.qlink.ui.activity.neo.contract.WalletCreatedContract;
import com.stratagile.qlink.ui.activity.neo.module.WalletCreatedModule;
import com.stratagile.qlink.ui.activity.neo.presenter.WalletCreatedPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/24 16:27:17
 */

public class WalletCreatedActivity extends BaseActivity implements WalletCreatedContract.View {

    @Inject
    WalletCreatedPresenter mPresenter;
    @BindView(R.id.tvChoose)
    TextView tvChoose;
    @BindView(R.id.btBackup)
    Button btBackup;

    private Wallet wallet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_wallet_created);
        ButterKnife.bind(this);
        setTitle(getString(R.string.create_wallet));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        wallet = getIntent().getParcelableExtra("wallet");
        KLog.i(wallet.toString());
        tvChoose.setSelected(true);
        btBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(WalletCreatedActivity.this, NeoWalletInfoActivity.class).putExtra("wallet", wallet), 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
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


}