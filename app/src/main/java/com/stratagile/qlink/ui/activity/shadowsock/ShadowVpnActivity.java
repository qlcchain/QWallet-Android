package com.stratagile.qlink.ui.activity.shadowsock;

import android.os.Bundle;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.shadowsock.component.DaggerShadowVpnComponent;
import com.stratagile.qlink.ui.activity.shadowsock.contract.ShadowVpnContract;
import com.stratagile.qlink.ui.activity.shadowsock.module.ShadowVpnModule;
import com.stratagile.qlink.ui.activity.shadowsock.presenter.ShadowVpnPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.shadowsock
 * @Description: $description
 * @date 2018/08/07 11:54:13
 */

public class ShadowVpnActivity extends BaseActivity implements ShadowVpnContract.View {

    @Inject
    ShadowVpnPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_shadow_vpn);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle("Shadowsocks");
    }

    @Override
    protected void setupActivityComponent() {
       DaggerShadowVpnComponent
               .builder()
               .appComponent(((AppConfig) getApplication()).getApplicationComponent())
               .shadowVpnModule(new ShadowVpnModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(ShadowVpnContract.ShadowVpnContractPresenter presenter) {
        mPresenter = (ShadowVpnPresenter) presenter;
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