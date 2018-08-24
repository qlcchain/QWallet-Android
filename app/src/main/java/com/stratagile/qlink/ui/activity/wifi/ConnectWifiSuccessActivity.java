package com.stratagile.qlink.ui.activity.wifi;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.ui.activity.wifi.component.DaggerConnectWifiSuccessComponent;
import com.stratagile.qlink.ui.activity.wifi.contract.ConnectWifiSuccessContract;
import com.stratagile.qlink.ui.activity.wifi.module.ConnectWifiSuccessModule;
import com.stratagile.qlink.ui.activity.wifi.presenter.ConnectWifiSuccessPresenter;
import com.stratagile.qlink.utils.GlideCircleTransform;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: $description
 * @date 2018/01/19 19:46:50
 */

public class ConnectWifiSuccessActivity extends BaseActivity implements ConnectWifiSuccessContract.View {

    @Inject
    ConnectWifiSuccessPresenter mPresenter;
    @BindView(R.id.tv_ssid)
    TextView tvSsid;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.iv_avater)
    ImageView ivAvater;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_connect_wifi_success);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        tvSsid.setText(getIntent().getStringExtra("ssid"));
        WifiEntity wifiEntity = getIntent().getParcelableExtra("wifientity");
        if (wifiEntity.getAvatar() != null && !wifiEntity.getAvatar().equals("")) {
            Glide.with(this)
                    .load(API.BASE_URL + wifiEntity.getAvatar().replace("\\", "/"))
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvater);
        } else {
            Glide.with(this)
                    .load(R.mipmap.img_default_avatar)
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvater);
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerConnectWifiSuccessComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .connectWifiSuccessModule(new ConnectWifiSuccessModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ConnectWifiSuccessContract.ConnectWifiSuccessContractPresenter presenter) {
        mPresenter = (ConnectWifiSuccessPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.ll_root)
    public void onViewClicked() {
        finish();
    }
}