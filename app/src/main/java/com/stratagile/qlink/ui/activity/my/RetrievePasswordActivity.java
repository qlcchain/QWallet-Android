package com.stratagile.qlink.ui.activity.my;

import android.os.Bundle;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.my.component.DaggerRetrievePasswordComponent;
import com.stratagile.qlink.ui.activity.my.contract.RetrievePasswordContract;
import com.stratagile.qlink.ui.activity.my.module.RetrievePasswordModule;
import com.stratagile.qlink.ui.activity.my.presenter.RetrievePasswordPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description 找回密码页面
 * @date 2019/04/09 14:21:19
 */

public class RetrievePasswordActivity extends BaseActivity implements RetrievePasswordContract.View {

    @Inject
    RetrievePasswordPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_retrieve_password);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle("找回密码");
    }

    @Override
    protected void setupActivityComponent() {
       DaggerRetrievePasswordComponent
               .builder()
               .appComponent(((AppConfig) getApplication()).getApplicationComponent())
               .retrievePasswordModule(new RetrievePasswordModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(RetrievePasswordContract.RetrievePasswordContractPresenter presenter) {
        mPresenter = (RetrievePasswordPresenter) presenter;
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