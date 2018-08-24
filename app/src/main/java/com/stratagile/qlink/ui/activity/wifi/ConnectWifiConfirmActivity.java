package com.stratagile.qlink.ui.activity.wifi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.wifi.WifipasswordRsp;
import com.stratagile.qlink.ui.activity.wifi.component.DaggerConnectWifiConfirmComponent;
import com.stratagile.qlink.ui.activity.wifi.contract.ConnectWifiConfirmContract;
import com.stratagile.qlink.ui.activity.wifi.module.ConnectWifiConfirmModule;
import com.stratagile.qlink.ui.activity.wifi.presenter.ConnectWifiConfirmPresenter;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.StringUitl;
import com.stratagile.qlink.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: $description
 * @date 2018/01/19 19:46:27
 */

public class ConnectWifiConfirmActivity extends BaseActivity implements ConnectWifiConfirmContract.View {

    @Inject
    ConnectWifiConfirmPresenter mPresenter;
    @BindView(R.id.tv_ssid)
    TextView tvSsid;
    @BindView(R.id.tv_mac)
    TextView tvMac;
    @BindView(R.id.tv_signal)
    TextView tvSignal;
    @BindView(R.id.tv_connect_count)
    TextView tvConnectCount;
    @BindView(R.id.bt_cancal)
    Button btCancal;
    @BindView(R.id.bt_connect)
    Button btConnect;
    @BindView(R.id.iv_avater)
    ImageView ivAvater;
    @BindView(R.id.iv_user_avater)
    ImageView ivUserAvater;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private WifiEntity wifiEntity;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_connect_wifi_confirm);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        wifiEntity = getIntent().getParcelableExtra("wifientity");
        tvSsid.setText(wifiEntity.getSsid());
        tvTitle.setText(getString(R.string.connect).toUpperCase());
        tvMac.setText(wifiEntity.getMacAdrees());
        tvConnectCount.setText(wifiEntity.getConnectCount() + "/" + wifiEntity.getDeviceAllowed());
        if (!SpUtil.getString(this, ConstantValue.myAvatarPath, "").equals("")) {
            Glide.with(this)
                    .load(API.BASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                    .apply(AppConfig.getInstance().options)
                    .into(ivUserAvater);
        } else {
            Glide.with(this)
                    .load(R.mipmap.img_connected_head_portrait)
                    .apply(AppConfig.getInstance().options)
                    .into(ivUserAvater);
        }
        if (wifiEntity.getAvatar() != null && !wifiEntity.getAvatar().equals("")) {
            if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
                Glide.with(this)
                        .load(MainAPI.MainBASE_URL + wifiEntity.getAvatar().replace("\\", "/"))
                        .apply(AppConfig.getInstance().options)
                        .into(ivAvater);
            } else {
                Glide.with(this)
                        .load(API.BASE_URL + wifiEntity.getAvatar().replace("\\", "/"))
                        .apply(AppConfig.getInstance().options)
                        .into(ivAvater);
            }
        } else {
            Glide.with(this)
                    .load(R.mipmap.img_connected_head_portrait)
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvater);
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerConnectWifiConfirmComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .connectWifiConfirmModule(new ConnectWifiConfirmModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ConnectWifiConfirmContract.ConnectWifiConfirmContractPresenter presenter) {
        mPresenter = (ConnectWifiConfirmPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void connectWifiSuccess(String msg, String ssid) {
        if(ConstantValue.isConnectWifi)
        {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ConnectWifiSuccess");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ConnectWifiSuccess");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ConnectWifiSuccess");
            mFirebaseAnalytics.logEvent("ConnectWifiSuccess", bundle);
            ConstantValue.isConnectWifi = false;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
                ToastUtil.displayShortToast(msg);
                //开启新的页面
//                startService(new Intent(ConnectWifiConfirmActivity.this, ClientConnectedWifiRecordService.class));

                Intent intent = new Intent(ConnectWifiConfirmActivity.this, ConnectWifiSuccessActivity.class);
                intent.putExtra("wifientity", wifiEntity);
                intent.putExtra("ssid", ssid);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void conncetWifiFailure(String msg) {
        if(ConstantValue.isConnectWifi)
        {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "conncetWifiFailure");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "conncetWifiFailure");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "conncetWifiFailure");
            mFirebaseAnalytics.logEvent("conncetWifiFailure", bundle);
            ConstantValue.isConnectWifi = false;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
                ToastUtil.displayShortToast(msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mPresenter.stopConnectWiFi();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.bt_cancal, R.id.bt_connect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_cancal:
                onBackPressed();
                break;
            case R.id.bt_connect:
                //{"Type":"wifipasswordReq","Data": { "SSID": "YYM-5", "MAC":"00:0c:29:86:d9:94"}}
                ConstantValue.isConnectWifi = true;
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "beginConnectWifi");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "beginConnectWifi");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "beginConnectWifi");
                mFirebaseAnalytics.logEvent("beginConnectWifi", bundle);
                String currentTime = StringUitl.getNowDateShort();
                String currentTimeFlag = SpUtil.getString(AppConfig.getInstance(), currentTime+"_wifi","0");
                if(currentTimeFlag.equals("0"))
                {
                    bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "useWIFITotal");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "useWIFITotal");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "useWIFITotal");
                    mFirebaseAnalytics.logEvent("useWIFITotal", bundle);
                    SpUtil.putString(AppConfig.getInstance(), currentTime+"_wifi","1");
                }
                if (wifiEntity.getCapabilities().contains("WEP")) {
                    Map<String, String> infoMap = new HashMap<>();
                    infoMap.put("ssid", wifiEntity.getSsid());
                    infoMap.put("mac", wifiEntity.getMacAdrees());
                    QlinkUtil.parseMap2StringAndSend(wifiEntity.getFreindNum(), ConstantValue.wifipasswordReq, infoMap);
                } else if (wifiEntity.getCapabilities().contains("WPA")) {
                    Map<String, String> infoMap = new HashMap<>();
                    infoMap.put("ssid", wifiEntity.getSsid());
                    infoMap.put("mac", wifiEntity.getMacAdrees());
                    QlinkUtil.parseMap2StringAndSend(wifiEntity.getFreindNum(), ConstantValue.wifipasswordReq, infoMap);
                } else {
                    WifipasswordRsp wifipasswordRsp = new WifipasswordRsp();
                    wifipasswordRsp.setFriendNum(wifiEntity.getFreindNum());
                    wifipasswordRsp.setSsid(wifiEntity.getSsid());
                    wifipasswordRsp.setPassword("");
                    mPresenter.createLinkToWifi(wifipasswordRsp);
                }
                showProgressDialog();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void connectToWifi(WifipasswordRsp wifipasswordRsp) {
        mPresenter.createLinkToWifi(wifipasswordRsp);
    }
}