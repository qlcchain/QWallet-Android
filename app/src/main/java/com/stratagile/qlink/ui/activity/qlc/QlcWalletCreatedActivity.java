package com.stratagile.qlink.ui.activity.qlc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.QLCAccount;
import com.stratagile.qlink.ui.activity.qlc.QlcMnemonicShowActivity;
import com.stratagile.qlink.ui.activity.qlc.component.DaggerQlcWalletCreatedComponent;
import com.stratagile.qlink.ui.activity.qlc.contract.QlcWalletCreatedContract;
import com.stratagile.qlink.ui.activity.qlc.module.QlcWalletCreatedModule;
import com.stratagile.qlink.ui.activity.qlc.presenter.QlcWalletCreatedPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2019/05/20 09:24:16
 */

public class QlcWalletCreatedActivity extends BaseActivity implements QlcWalletCreatedContract.View {

    @Inject
    QlcWalletCreatedPresenter mPresenter;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.tvChoose)
    TextView tvChoose;
    @BindView(R.id.ivEth)
    ImageView ivEth;
    @BindView(R.id.tvEth)
    TextView tvEth;
    @BindView(R.id.llEth)
    LinearLayout llEth;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.btBackup)
    Button btBackup;

    private QLCAccount qlcAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_qlc_wallet_created);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle(getString(R.string.create_wallet));
    }

    @Override
    protected void initData() {
        qlcAccount = getIntent().getParcelableExtra("wallet");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerQlcWalletCreatedComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .qlcWalletCreatedModule(new QlcWalletCreatedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(QlcWalletCreatedContract.QlcWalletCreatedContractPresenter presenter) {
        mPresenter = (QlcWalletCreatedPresenter) presenter;
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
        startActivityForResult(new Intent(this, QlcMnemonicShowActivity.class).putExtra("wallet", qlcAccount), 0);
    }

    @Override
    public void onBackPressed() {

    }
}