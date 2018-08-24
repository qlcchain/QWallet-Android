package com.stratagile.qlink.ui.activity.wifi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.entity.eventbus.ChangeViewpager;
import com.stratagile.qlink.ui.activity.main.MainActivity;
import com.stratagile.qlink.ui.activity.wifi.component.DaggerRegisteWifiSuccessComponent;
import com.stratagile.qlink.ui.activity.wifi.contract.RegisteWifiSuccessContract;
import com.stratagile.qlink.ui.activity.wifi.module.RegisteWifiSuccessModule;
import com.stratagile.qlink.ui.activity.wifi.presenter.RegisteWifiSuccessPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: $description
 * @date 2018/01/19 19:45:43
 */

public class RegisteWifiSuccessActivity extends BaseActivity implements RegisteWifiSuccessContract.View {

    @Inject
    RegisteWifiSuccessPresenter mPresenter;
    @BindView(R.id.tv_ssid)
    TextView tvSsid;
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.bt_continue)
    Button btContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_registe_wifi_success);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        tvSsid.setText(getIntent().getStringExtra("ssid"));
    }

    @Override
    protected void setupActivityComponent() {
        DaggerRegisteWifiSuccessComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .registeWifiSuccessModule(new RegisteWifiSuccessModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(RegisteWifiSuccessContract.RegisteWifiSuccessContractPresenter presenter) {
        mPresenter = (RegisteWifiSuccessPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.bt_back, R.id.bt_continue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                EventBus.getDefault().post(new ChangeViewpager(2));
                finish();
                break;
            case R.id.bt_continue:
                EventBus.getDefault().post(new ChangeViewpager(1));
                finish();
                break;
            default:
                break;
        }
    }
}