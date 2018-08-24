package com.stratagile.qlink.ui.activity.wifi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.socks.library.KLog;
import com.stratagile.qlink.view.NoScrollViewPager;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.ui.activity.wifi.component.DaggerRegisterWifiComponent;
import com.stratagile.qlink.ui.activity.wifi.contract.RegisterWifiContract;
import com.stratagile.qlink.ui.activity.wifi.module.RegisterWifiModule;
import com.stratagile.qlink.ui.activity.wifi.presenter.RegisterWifiPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: $description
 * @date 2018/01/09 17:28:09
 */

public class RegisterWifiActivity extends BaseActivity implements RegisterWifiContract.View {

    @Inject
    RegisterWifiPresenter mPresenter;
    WifiEntity mWifiEntity;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    String seizeQlc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register_wifi);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        mWifiEntity = getIntent().getParcelableExtra("wifiInfo");
        KLog.i(mWifiEntity.toString());
        seizeQlc = getIntent().getStringExtra("seizeQlc");
        if (mWifiEntity.isRegiste()) {
            setTitle(getString(R.string.wifidetails));
        } else {
            setTitle(getString(R.string.REGISTER_YOUR_WIFI).toUpperCase());
        }
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Intent intent = new Intent();
                intent.putExtra("wifiInfo", mWifiEntity);
                intent.putExtra("seizeQlc", seizeQlc);
                return WifiRegisteFragment.newInstacnce(intent);

            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "";
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.activity_translate_out_1);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerRegisterWifiComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .registerWifiModule(new RegisterWifiModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(RegisterWifiContract.RegisterWifiContractPresenter presenter) {
        mPresenter = (RegisterWifiPresenter) presenter;
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
    public void registeWifiSuccess() {
        onBackPressed();
    }

//    @OnClick(R.id.registerAsset)
//    public void onViewClicked() {
//        String ownP2Pid = SpUtil.getString(this, ContainValue.P2PID, "");
//        if ("".equals(ownP2Pid)) {
//            ToastUtil.displayShortToast("");
//            return;
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("appid", "MIFI");
//        map.put("timestamp", Calendar.getInstance().getTimeInMillis() + "");
//        Map<String, String> recordMap = new HashMap<String, String>();
//        recordMap.put("ssId", wifiSSID.getText().toString().trim());
//        recordMap.put("mac", macAddress.getText().toString().trim());
//        recordMap.put("p2pId", ownP2Pid);
//        map.put("params", recordMap);
//        mPresenter.registWIfi(map);
//    }
}