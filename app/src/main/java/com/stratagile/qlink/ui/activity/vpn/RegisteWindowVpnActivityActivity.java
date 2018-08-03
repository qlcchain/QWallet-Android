package com.stratagile.qlink.ui.activity.vpn;

import android.os.Bundle;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.vpn.component.DaggerRegisteWindowVpnActivityComponent;
import com.stratagile.qlink.ui.activity.vpn.contract.RegisteWindowVpnActivityContract;
import com.stratagile.qlink.ui.activity.vpn.module.RegisteWindowVpnActivityModule;
import com.stratagile.qlink.ui.activity.vpn.presenter.RegisteWindowVpnActivityPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: $description
 * @date 2018/08/03 11:56:07
 */

public class RegisteWindowVpnActivityActivity extends BaseActivity implements RegisteWindowVpnActivityContract.View {

    @Inject
    RegisteWindowVpnActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.add_window_asset);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setupActivityComponent() {
       DaggerRegisteWindowVpnActivityComponent
               .builder()
               .appComponent(((AppConfig) getApplication()).getApplicationComponent())
               .registeWindowVpnActivityModule(new RegisteWindowVpnActivityModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(RegisteWindowVpnActivityContract.RegisteWindowVpnActivityContractPresenter presenter) {
        mPresenter = (RegisteWindowVpnActivityPresenter) presenter;
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