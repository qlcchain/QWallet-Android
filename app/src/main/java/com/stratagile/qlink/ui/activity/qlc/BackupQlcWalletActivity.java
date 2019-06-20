package com.stratagile.qlink.ui.activity.qlc;

import android.os.Bundle;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.QLCAccount;
import com.stratagile.qlink.ui.activity.qlc.component.DaggerBackupQlcWalletComponent;
import com.stratagile.qlink.ui.activity.qlc.contract.BackupQlcWalletContract;
import com.stratagile.qlink.ui.activity.qlc.module.BackupQlcWalletModule;
import com.stratagile.qlink.ui.activity.qlc.presenter.BackupQlcWalletPresenter;
import com.stratagile.qlink.view.SmoothCheckBox;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2019/05/20 10:52:55
 */

public class BackupQlcWalletActivity extends BaseActivity implements BackupQlcWalletContract.View {

    @Inject
    BackupQlcWalletPresenter mPresenter;
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
    private QLCAccount qlcAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_backup_qlc_wallet);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    protected void initData() {
        setTitle("QLC Wallet");
        qlcAccount = getIntent().getParcelableExtra("wallet");
        tvPublicAddress.setText(qlcAccount.getAddress());
        tvWif.setText(qlcAccount.getPrivKey());
        tvHex.setText(qlcAccount.getSeed());
    }

    @Override
    protected void setupActivityComponent() {
        DaggerBackupQlcWalletComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .backupQlcWalletModule(new BackupQlcWalletModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(BackupQlcWalletContract.BackupQlcWalletContractPresenter presenter) {
        mPresenter = (BackupQlcWalletPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.tvSend)
    public void onViewClicked() {
        if (checkBox.isChecked()) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

    }
}