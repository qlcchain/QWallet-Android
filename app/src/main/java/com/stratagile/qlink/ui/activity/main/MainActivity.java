package com.stratagile.qlink.ui.activity.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.BroadCastAction;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.ContinentAndCountry;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.eventbus.ChangeToTestWallet;
import com.stratagile.qlink.entity.eventbus.ChangeViewpager;
import com.stratagile.qlink.entity.eventbus.ChangeWalletNeedRefesh;
import com.stratagile.qlink.entity.eventbus.DisconnectVpn;
import com.stratagile.qlink.entity.eventbus.ForegroundCallBack;
import com.stratagile.qlink.entity.eventbus.FreeCount;
import com.stratagile.qlink.entity.eventbus.MyStatus;
import com.stratagile.qlink.entity.eventbus.NeoRefrash;
import com.stratagile.qlink.entity.eventbus.P2pBack;
import com.stratagile.qlink.entity.eventbus.ReCreateMainActivity;
import com.stratagile.qlink.entity.eventbus.ShowGuide;
import com.stratagile.qlink.guideview.Component;
import com.stratagile.qlink.guideview.Guide;
import com.stratagile.qlink.guideview.GuideBuilder;
import com.stratagile.qlink.guideview.GuideConstantValue;
import com.stratagile.qlink.guideview.GuideSpUtil;
import com.stratagile.qlink.guideview.compnonet.EnterSettingComponent;
import com.stratagile.qlink.guideview.compnonet.EnterVpnComponent;
import com.stratagile.qlink.guideview.compnonet.EnterWalletComponent;
import com.stratagile.qlink.guideview.compnonet.RegistVpnComponent;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.main.component.DaggerMainComponent;
import com.stratagile.qlink.ui.activity.main.contract.MainContract;
import com.stratagile.qlink.ui.activity.main.module.MainModule;
import com.stratagile.qlink.ui.activity.main.presenter.MainPresenter;
import com.stratagile.qlink.ui.activity.setting.SettingsActivity;
import com.stratagile.qlink.ui.activity.sms.SmsFragment;
import com.stratagile.qlink.ui.activity.vpn.RegisteVpnActivity;
import com.stratagile.qlink.ui.activity.wallet.CreateWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.FreeConnectActivity;
import com.stratagile.qlink.ui.activity.wallet.NoWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.ProfilePictureActivity;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.WalletDetailActivity;
import com.stratagile.qlink.ui.activity.wallet.WalletFragment;
import com.stratagile.qlink.ui.activity.wifi.RegisterWifiActivity;
import com.stratagile.qlink.ui.activity.wifi.WifiFragment;
import com.stratagile.qlink.utils.CountDownTimerUtils;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.SpringAnimationUtil;
import com.stratagile.qlink.utils.SystemUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.utils.VersionUtil;
import com.stratagile.qlink.utils.eth.WalletStorage;
import com.stratagile.qlink.view.BottomNavigationViewEx;
import com.stratagile.qlink.view.DownCheckView;
import com.stratagile.qlink.view.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: $description
 * @date 2018/01/09 09:57:09
 */

public class MainActivity extends BaseActivity implements MainContract.View {

    @Inject
    MainPresenter mPresenter;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationViewEx bottomNavigation;
    @BindView(R.id.status_bar)
    TextView statusBar;
    @BindView(R.id.iv_avater)
    ImageView ivAvater;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_wallet)
    ImageView ivWallet;
    @BindView(R.id.view_vpn)
    View viewVpn;
    @BindView(R.id.view_wallet)
    View viewWallet;
    @BindView(R.id.downCHeckView)
    DownCheckView downCHeckView;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.tv_free)
    TextView tvFree;

    private FirebaseAnalytics mFirebaseAnalytics;

    public static final int START_NO_WALLLET = 0;
    public static final int START_CREATE_PASSWORD = 2;
    public static final int START_VERTIFY_PASSWORD = 3;
    public static final int START_SELECT_PICTURE = 4;

    public static MainActivity mainActivity;
    @BindView(R.id.my_status)
    ImageView myStatus;

    DisconnectVpnSuccessBroadReceiver disconnectVpnSuccessBroadReceiver = new DisconnectVpnSuccessBroadReceiver();
    private LocationManager locationManager;

    private MyStatus myStatusFlag;
    private CountDownTimerUtils countDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        needFront = true;
        super.onCreate(savedInstanceState);
        mainActivity = this;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(UIUtils.getDisplayWidth(this), UIUtils.getStatusBarHeight(this));
        statusBar.setLayoutParams(llp);
        ArrayList<ContinentAndCountry.ContinentBean.CountryBean> countryBeans = new ArrayList<>();
        countryBeans.add(new ContinentAndCountry.ContinentBean.CountryBean("United States", "united_states"));
        countryBeans.add(new ContinentAndCountry.ContinentBean.CountryBean("United Kingdom", "united_kingdom"));
        countryBeans.add(new ContinentAndCountry.ContinentBean.CountryBean("Singapore", "singapore"));
        countryBeans.add(new ContinentAndCountry.ContinentBean.CountryBean("Japan", "japan"));
        countryBeans.add(new ContinentAndCountry.ContinentBean.CountryBean("Switzerland", "switzerland"));
        countryBeans.add(new ContinentAndCountry.ContinentBean.CountryBean("Germany", "germany"));
        countryBeans.add(new ContinentAndCountry.ContinentBean.CountryBean("Others", "icon_others"));
        downCHeckView.setData(countryBeans);
        downCHeckView.setText(new ContinentAndCountry.ContinentBean.CountryBean(getString(R.string.choose_location), "icon_choose_location"));
        if (!SpUtil.getBoolean(this, ConstantValue.isMainNet, false) && SpUtil.getBoolean(this, ConstantValue.showTestFlag, true)) {
            statusBar.setBackgroundColor(getResources().getColor(R.color.color_f51818));
            statusBar.setText(getString(R.string.testnet));
        }
//        File dataFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + SpUtil.getString(this, ConstantValue.myAvaterUpdateTime, "") + ".jpg", "");
        if (!SpUtil.getString(this, ConstantValue.myAvatarPath, "").equals("")) {
            if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
                Glide.with(this)
                        .load(MainAPI.MainBASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().options)
                        .into(ivAvater);
            } else {
                Glide.with(this)
                        .load(API.BASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().options)
                        .into(ivAvater);
            }
        }
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("MainActivity");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPic(P2pBack p2pBack) {
        Map<String, String> infoMap1 = new HashMap<>();
        infoMap1.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
        mPresenter.zsFreeNum(infoMap1);

        if (SpUtil.getString(this, ConstantValue.myAvatarPath, "").equals("")) {
            Map<String, String> infoMap = new HashMap<>();
            infoMap.put("p2pId", SpUtil.getString(this, ConstantValue.P2PID, ""));
            mPresenter.userAvatar(infoMap);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showVpnGuide(ShowGuide showGuide) {
        if (showGuide.getNumber() == 3) {
            showGuideViewVpnFragment();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showRegisteVpnGuide(ShowGuide showGuide) {
        if (showGuide.getNumber() == 0) {
            EventBus.getDefault().post(new ShowGuide(1));
//            if (ConstantValue.isCloseRegisterAssetsInMain && SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
//                EventBus.getDefault().post(new ShowGuide(2));
//            } else {
//                showGuideViewRegisteVpn();
//            }
        }
    }


    @Override
    public void onGetFreeNumBack(int num) {
        ConstantValue.freeNum = num;
        if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
//            if (num == 0) {
//                tvFree.setVisibility(View.GONE);
//                ivWallet.setVisibility(View.GONE);
//            } else {
//                tvFree.setVisibility(View.VISIBLE);
//                ivWallet.setVisibility(View.VISIBLE);
//                tvFree.setText(getString(R.string.free) + ":" + num);
//            }
            tvFree.setVisibility(View.VISIBLE);
            ivWallet.setVisibility(View.VISIBLE);
            tvFree.setText(getString(R.string.free) + ":" + num);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetFreeNumBack(FreeCount freeCount) {
        if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
            if (freeCount.getCount() == 0) {
                tvFree.setVisibility(View.GONE);
                ivWallet.setVisibility(View.GONE);
            } else {
                tvFree.setVisibility(View.VISIBLE);
                ivWallet.setVisibility(View.VISIBLE);
                tvFree.setText(getString(R.string.free) + ":" + freeCount.getCount());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reCreateMainActivity(ReCreateMainActivity reCreateMainActivity) {
        recreate();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeTestUI(ChangeWalletNeedRefesh changeWalletNeedRefesh) {
        mPresenter.getMainAddress();
        if (!SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
            if (SpUtil.getBoolean(this, ConstantValue.showTestFlag, true)) {
                statusBar.setBackgroundColor(getResources().getColor(R.color.color_f51818));
                statusBar.setText(getString(R.string.testnet));
            }
        } else {
            statusBar.setBackground(getResources().getDrawable(R.drawable.navigation_shape));
            statusBar.setText("");
        }
    }

    @Override
    protected void initData() {
        qlinkcom.init();
        mPresenter.getTox();
        getLocation();
        String addressNames = FileUtil.getAllAddressNames();
        Map<String, String> map = new HashMap<>();
        map.put("key", addressNames);
        if (!SpUtil.getString(this, ConstantValue.P2PID, "").equals("")) {
            Map<String, String> infoMap1 = new HashMap<>();
            infoMap1.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
            mPresenter.zsFreeNum(infoMap1);
        }
        if (!("".equals(addressNames))) {
            List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
            if (walletList.size() == 0) {
                ConstantValue.canClickWallet = false;
                mPresenter.importWallet(map);
            }
        }
        if (countDownTimerUtils == null) {
            countDownTimerUtils = CountDownTimerUtils.creatNewInstance();
            countDownTimerUtils.setMillisInFuture(Long.MAX_VALUE)
                    .setCountDownInterval(60 * 1000)
                    .setTickDelegate(new CountDownTimerUtils.TickDelegate() {
                        @Override
                        public void onTick(long pMillisUntilFinished) {
                            KLog.i("heart倒计时");
                            //if (myStatusFlag != null && myStatusFlag.getStatus() > 0) {
                            String p2pId = SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "");
                            String currentWifiName = "";
                            String currentVpnName = "";
                            for (WifiEntity wifiEntity : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                                if (wifiEntity.getIsConnected()) {
                                    currentWifiName = wifiEntity.getSsid();
                                    break;
                                }
                            }
                            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
                            for (VpnEntity vpnEntity : AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll()) {
                                if (vpnEntity.getIsConnected()) {
                                    currentVpnName = vpnEntity.getVpnName();
                                    break;
                                }
                            }
                            if (p2pId != null && !p2pId.equals("")) {
                                Map<String, Object> mapHeart = new HashMap<>();
                                mapHeart.put("p2pId", p2pId);
                                mapHeart.put("status", myStatusFlag == null ? 0 : (myStatusFlag.getStatus() > 0 ? 1 : 0));
                                mapHeart.put("wifiName", currentWifiName);
                                mapHeart.put("vpnName", currentVpnName);
                                mPresenter.heartBeat(mapHeart);
                            }

                            long lastRestart = SpUtil.getLong(AppConfig.getInstance(), ConstantValue.lastRestart, Calendar.getInstance().getTimeInMillis());
                            if ((Calendar.getInstance().getTimeInMillis() - lastRestart) > 2 * 60 * 60 * 1000)//每两小时重启一次
                            {
                                SpUtil.putLong(AppConfig.getInstance(), ConstantValue.lastRestart, Calendar.getInstance().getTimeInMillis());
                                Intent intent = new Intent(mainActivity, SplashActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Process.killProcess(Process.myPid());
                                System.exit(0);
                            }
                            // }
                        }
                    }).start();
        }

        LogUtil.addLog(SystemUtil.getDeviceBrand() + "  " + SystemUtil.getSystemModel() + "   " + SystemUtil.getSystemVersion() + "   " + VersionUtil.getAppVersionName(this), getClass().getSimpleName());
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new SmsFragment();
                } else if (position == 1) {
                    return new WifiFragment();
                } else {
                    return new WalletFragment();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        //设置BottomNavigationMenuView的字体
        bottomNavigation.enableAnimation(false);
        bottomNavigation.enableShiftingMode(false);
        bottomNavigation.enableItemShiftingMode(false);
        bottomNavigation.setTextSize(10);
        bottomNavigation.setTypeface(Typeface.createFromAsset(getAssets(), "vagroundedbt.ttf"));
        viewPager.setOffscreenPageLimit(2);
        bottomNavigation.setIconSizeAt(0, 17.6f, 21.2f);
        bottomNavigation.setIconSizeAt(1, 23.6f, 18.8f);
        bottomNavigation.setIconSizeAt(2, 22, 18.8f);
        bottomNavigation.setIconsMarginTop((int) getResources().getDimension(R.dimen.x22));
        bottomNavigation.setSelectedItemId(R.id.item_sms);
        setSmsPage();
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_sms:
                        setSmsPage();
                        break;
                    case R.id.item_wifi:
                        setWifiPage();
                        break;
                    case R.id.item_wallet:
                        if (ConstantValue.canClickWallet) {
                            setWalletPage();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        IntentFilter intent = new IntentFilter();
        intent.addAction(BroadCastAction.disconnectVpnSuccess);
        registerReceiver(disconnectVpnSuccessBroadReceiver, intent);
//        startService(new Intent(this, ClientConnectedWifiRecordService.class));
        /**
         * @see WalletFragment#refreshNeo(NeoRefrash)
         */
        EventBus.getDefault().post(new NeoRefrash());
        //创建neo钱包。为唯一对象，每次在使用的钱包只有一个。
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (walletList != null && walletList.size() != 0) {
            Wallet wallet = walletList.get(SpUtil.getInt(this, ConstantValue.currentWallet, 0));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (Account.INSTANCE.getWallet() == null) {
                        Account.INSTANCE.fromWIF(wallet.getWif());
                    }
                }
            }).start();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WalletStorage.getInstance(MainActivity.this).load(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        ivWallet.post(new Runnable() {
            @Override
            public void run() {
                showGuideViewEnterWallet();
            }
        });
    }

    /**
     * 修改自己的在线状态
     *
     * @param myStatus
     * @see Qsdk#handlerSelfStatusChange(int)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setMyStatus(MyStatus myStatus) {
        KLog.i("设置自己的状态：" + myStatus.getStatus());
        myStatusFlag = myStatus;
        if (myStatus.getStatus() > 0) {
            this.myStatus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_search));
            SpringAnimationUtil.startScaleSpringViewAnimation(this.myStatus);
            /*if(countDownTimerUtils != null)
            {
                countDownTimerUtils.doOnce();
            }*/
        } else {
            this.myStatus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_search_red));
            SpringAnimationUtil.startScaleSpringViewAnimation(this.myStatus);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void changeToTestWallet(ChangeToTestWallet changeToTestWallet) {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        Account.INSTANCE.fromWIF(walletList.get(SpUtil.getInt(this, ConstantValue.currentWallet, 0)).getWif());
    }

    /**
     * 设置为vpn界面
     */
    private void setSmsPage() {
        viewPager.setCurrentItem(0, false);
        downCHeckView.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        tvTitle.setText(R.string.vpn);
        tvFree.setVisibility(View.VISIBLE);
//        if (freeNum != 0) {
//            tvFree.setVisibility(View.VISIBLE);
//        } else {
//            tvFree.setVisibility(View.GONE);
//        }
        Glide.with(this)
                .load(R.mipmap.icon_free)
                .into(ivWallet);
    }

    /**
     * 设置为wifi界面
     */
    private void setWifiPage() {
        tvFree.setVisibility(View.GONE);
        downCHeckView.setVisibility(View.GONE);
        downCHeckView.setOnItemCheckListener(new DownCheckView.OnItemCheckListener() {
            @Override
            public void onItemCheck(ContinentAndCountry.ContinentBean.CountryBean item) {

            }
        });
        tvTitle.setVisibility(View.VISIBLE);
        viewPager.setCurrentItem(1, false);
        tvTitle.setText(R.string.AvailableWiFi);
        Glide.with(this)
                .load(R.mipmap.icon_addition)
                .into(ivWallet);
        ivWallet.setVisibility(View.INVISIBLE);
        ivWallet.setClickable(false);
        if (ConstantValue.isCloseRegisterAssetsInMain && SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
            if (ivWallet != null) {
                ivWallet.setVisibility(View.INVISIBLE);
                ivWallet.setClickable(false);
            }
        } else {
            if (ivWallet != null) {
                ivWallet.setVisibility(View.VISIBLE);
                ivWallet.setClickable(true);
            }
        }
    }

    /**
     * 设置为钱包界面
     */
    int jianjushijian = 500;
    long dangqianshijian = 0;

    private void setWalletPage() {
        if (Calendar.getInstance().getTimeInMillis() - dangqianshijian <= jianjushijian) {
            return;
        }
        dangqianshijian = Calendar.getInstance().getTimeInMillis();
        KLog.i("进入钱包页面。。");
        tvFree.setVisibility(View.GONE);
        if (ivWallet != null) {
            ivWallet.setVisibility(View.VISIBLE);
            ivWallet.setClickable(true);
        }
        downCHeckView.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        //如果支持指纹，但是没有开启
        if (isSupportFingerPrint() && !SpUtil.getBoolean(this, ConstantValue.fingerprintUnLock, true)) {
            if (!SpUtil.getString(this, ConstantValue.walletPassWord, "").equals("")) {
                if (ConstantValue.isShouldShowVertifyPassword) {
                    Intent intent = new Intent(this, VerifyWalletPasswordActivity.class);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                    bottomNavigation.setSelectedItemId(R.id.item_wifi);
                } else {
                    viewPager.setCurrentItem(2, false);
                    tvTitle.setText(R.string.my_wallet);
                    Glide.with(this)
                            .load(R.mipmap.icon_set1)
                            .into(ivWallet);
                    showGuideViewEnterSetting();
                }
            } else {
                Intent intent = new Intent(this, CreateWalletPasswordActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                bottomNavigation.setSelectedItemId(R.id.item_wifi);
            }
            //如果不支持指纹，或者指纹开启
        } else {
            if (!SpUtil.getString(this, ConstantValue.walletPassWord, "").equals("") || !SpUtil.getString(this, ConstantValue.fingerPassWord, "").equals("")) {
                if (ConstantValue.isShouldShowVertifyPassword) {
                    Intent intent = new Intent(this, VerifyWalletPasswordActivity.class);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                    bottomNavigation.setSelectedItemId(R.id.item_wifi);
                } else {
                    viewPager.setCurrentItem(2, false);
                    tvTitle.setText(R.string.my_wallet);
                    Glide.with(this)
                            .load(R.mipmap.icon_set1)
                            .into(ivWallet);
                    showGuideViewEnterSetting();
                }
            } else {
                Intent intent = new Intent(this, CreateWalletPasswordActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                bottomNavigation.setSelectedItemId(R.id.item_wifi);
            }
        }
    }

    /**
     * 判断是否支持指纹解锁
     *
     * @return
     */
    private boolean isSupportFingerPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                FingerprintManager fingerprintManager = (FingerprintManager) AppConfig.getInstance().getSystemService(Context.FINGERPRINT_SERVICE);
                if (fingerprintManager != null && fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
                    return true;
                }
            } catch (Exception e) {

            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ConstantValue.isShouldShowVertifyPassword = false;
            setWalletPage();

        } else if (requestCode == 1) {
            bottomNavigation.setSelectedItemId(R.id.item_wifi);
        }
        if (requestCode == START_SELECT_PICTURE && resultCode == -1) {
//            File dataFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + SpUtil.getString(this, ConstantValue.myAvaterUpdateTime, "") + ".jpg", "");
            if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
                Glide.with(this)
                        .load(MainAPI.MainBASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().options)
                        .into(ivAvater);
            } else {
                Glide.with(this)
                        .load(API.BASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().options)
                        .into(ivAvater);
            }
        }
        if (requestCode == START_CREATE_PASSWORD && resultCode == RESULT_OK) {
            ivWallet.performClick();
            return;
        }
        if (requestCode == START_NO_WALLLET && resultCode == RESULT_OK) {
            ivWallet.performClick();
            return;
        }
        if (requestCode == START_VERTIFY_PASSWORD && resultCode == RESULT_OK) {
            ivWallet.performClick();
            return;
        }
    }

    @Override
    public void onBackPressed() {
        if (downCHeckView != null && downCHeckView.isShow()) {
            downCHeckView.close();
            return;
        }
        moveTaskToBack(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleToAppToBack(ForegroundCallBack foregroundCallBack) {
        if (!foregroundCallBack.isForeground()) {
            ConstantValue.isShouldShowVertifyPassword = true;
            if (viewPager.getCurrentItem() == 2) {
                bottomNavigation.setSelectedItemId(R.id.item_wifi);
                viewPager.setCurrentItem(1, false);
            }
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerMainComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(MainContract.MainContractPresenter presenter) {
        mPresenter = (MainPresenter) presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(disconnectVpnSuccessBroadReceiver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setViewPager(ChangeViewpager changeViewpager) {
        switch (changeViewpager.getPosition()) {
            case 0:
                bottomNavigation.setSelectedItemId(R.id.item_sms);
                break;
            case 1:
                bottomNavigation.setSelectedItemId(R.id.item_wifi);
                break;
            case 2:
                bottomNavigation.setSelectedItemId(R.id.item_wallet);
                break;
            default:
                break;
        }
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
    public void getPermissionSuccess() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60 * 1000, 1000, mLocationListener);
            return;
        }
    }

    //获取是否已打开自身GPS
    public boolean isGpsEnable() {
        String providers = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (providers != null && providers.contains(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    private void getLocation() {
        if (!isGpsEnable()) {
            Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(callGPSSettingIntent);
        }
        // 获取位置管理服务
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) this.getSystemService(serviceName);
        // 查找到服务信息
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setCostAllowed(true);
//        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
//        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
//        Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
//        updateToNewLocation(location);
        // 设置监听*器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
        mPresenter.getLocation();
    }

    LocationListener mLocationListener = new LocationListener() {
        @TargetApi(17)
        @Override
        public void onLocationChanged(Location mlocal) {
            if (mlocal == null) {
                return;
            }
            String strResult = "getAccuracy:" + mlocal.getAccuracy() + "\r\n"
                    + "getAltitude:" + mlocal.getAltitude() + "\r\n"
                    + "getBearing:" + mlocal.getBearing() + "\r\n"
                    + "getElapsedRealtimeNanos:" + String.valueOf(mlocal.getElapsedRealtimeNanos()) + "\r\n"
                    + "getLatitude:" + mlocal.getLatitude() + "\r\n"
                    + "getLongitude:" + mlocal.getLongitude() + "\r\n"
                    + "getProvider:" + mlocal.getProvider() + "\r\n"
                    + "getSpeed:" + mlocal.getSpeed() + "\r\n"
                    + "getTime:" + mlocal.getTime() + "\r\n";
            KLog.i(strResult);
            ConstantValue.mLatitude = (float) mlocal.getLatitude();
            ConstantValue.mLongitude = (float) mlocal.getLongitude();
            Map<String, String> map = new HashMap<>();
            map.put("latlng", mlocal.getLatitude() + "," + mlocal.getLongitude());
            map.put("language", "en_us");
            map.put("sensor", "false");
            mPresenter.latlngParseCountry(map);
        }

        @Override
        public void onProviderDisabled(String arg0) {
        }

        @Override
        public void onProviderEnabled(String arg0) {
        }

        @Override
        public void onStatusChanged(String provider, int event, Bundle extras) {
        }
    };

    private void updateToNewLocation(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
        } else {

        }
    }

    @SuppressLint("RestrictedApi")
    @OnClick({R.id.iv_avater, R.id.iv_wallet, R.id.tv_title, R.id.view_wallet, R.id.view_vpn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avater:
                Intent intent1 = new Intent(this, ProfilePictureActivity.class);
                startActivityForResult(intent1, START_SELECT_PICTURE);
                break;
            case R.id.iv_wallet:
                if (bottomNavigation.getSelectedItemId() == R.id.item_wallet) {
                    startActivity(new Intent(this, SettingsActivity.class));
                    return;
                }
                if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
                    startActivity(new Intent(this, FreeConnectActivity.class));
                    return;
                }
                List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
                if (SpUtil.getString(this, ConstantValue.walletPassWord, "").equals("")) {
                    if (!isSupportFingerPrint()) {
                        Intent intent = new Intent(this, CreateWalletPasswordActivity.class);
                        startActivityForResult(intent, START_CREATE_PASSWORD);
                        break;
                    } else {
                        if (!SpUtil.getBoolean(this, ConstantValue.fingerprintUnLock, true)) {
                            Intent intent = new Intent(this, CreateWalletPasswordActivity.class);
                            startActivityForResult(intent, START_CREATE_PASSWORD);
                            break;
                        } else {
                            //继续
                        }
                    }
                }
                if (walletList == null || walletList.size() == 0) {
                    Intent intent = new Intent(this, NoWalletActivity.class);
                    intent.putExtra("flag", "nowallet");
                    startActivityForResult(intent, START_NO_WALLLET);
                    break;
                }
                if (ConstantValue.isShouldShowVertifyPassword) {
                    Intent intent = new Intent(this, VerifyWalletPasswordActivity.class);
                    startActivityForResult(intent, START_VERTIFY_PASSWORD);
                    break;
                }
                if (ConstantValue.connectedWifiInfo != null) {
                    if (ConstantValue.isConnectToP2p) {
                        if (bottomNavigation.getSelectedItemId() == R.id.item_wifi) {
                            Intent intent = new Intent(this, RegisterWifiActivity.class);
                            List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().list();
                            for (WifiEntity wifiEntity : wifiEntityList) {
                                if (wifiEntity.getSsid().equals(ConstantValue.connectedWifiInfo.getSSID().replace("\"", ""))) {
                                    intent.putExtra("wifiInfo", wifiEntity);
                                    KLog.i("打开wifi注册界面");
                                    startActivity(intent);
                                    this.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                                    break;
                                }
                            }
                        } else if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
                            Intent intent = new Intent(this, RegisteVpnActivity.class);
                            intent.putExtra("flag", "");
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                        } else if (bottomNavigation.getSelectedItemId() == R.id.item_wallet) {
                            startActivity(new Intent(this, WalletDetailActivity.class));
                        }

                    } else {
                        ToastUtil.displayShortToast(getString(R.string.waitP2pNetWork));
                    }
                } else {
                    ToastUtil.displayShortToast(getString(R.string.pleaseEnableWifi));
                }
                break;
            case R.id.tv_title:
                if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
                    startActivity(new Intent(this, LogActivity.class));
                } else if (bottomNavigation.getSelectedItemId() == R.id.item_wifi) {
                    startActivity(new Intent(this, LogActivity.class));
                } else if (bottomNavigation.getSelectedItemId() == R.id.item_wallet) {
//                    startActivity(new Intent(this, CryptoWalletTestActivity.class));
                    clearGuide();
                }
                break;
            case R.id.view_wallet:
                bottomNavigation.setSelectedItemId(R.id.item_wallet);
                break;
            case R.id.view_vpn:
                bottomNavigation.setSelectedItemId(R.id.item_sms);
                break;
            default:
                break;
        }
    }

    public class DisconnectVpnSuccessBroadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            KLog.i("断开vpn成功， 开始更改ui");
            if (intent.getAction().equals(BroadCastAction.disconnectVpnSuccess)) {
                List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
                for (VpnEntity vpnEntity : vpnEntityList) {
                    if (vpnEntity.getIsConnected()) {
                        vpnEntity.setIsConnected(false);
                        AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                    }
                }
                EventBus.getDefault().post(new DisconnectVpn());
            }
        }
    }

    @Override
    public void onCreatWalletSuccess(ArrayList<Wallet> importWalletResult, int flag) {
        //AppConfig.getInstance().getDaoSession().getWalletDao().insertInTx(importWalletResult);
        String index = FileUtil.readData("/Qlink/Address/index.txt");
        if (!"".equals(index) && Integer.valueOf(index) < importWalletResult.size()) {
            SpUtil.putInt(this, ConstantValue.currentWallet, Integer.valueOf(index));
        } else {
            SpUtil.putInt(this, ConstantValue.currentWallet, 0);
            FileUtil.savaData("/Qlink/Address/index.txt", "0");
        }
    }

    @Override
    public void getAvatarSuccess(UpLoadAvatar upLoadAvatar) {
        if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
            Glide.with(this)
                    .load(MainAPI.MainBASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvater);
        } else {
            Glide.with(this)
                    .load(API.BASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvater);
        }


    }

    private void showGuideViewRegisteVpn() {
        if (!GuideSpUtil.getBoolean(this, GuideConstantValue.isShowRegisteVpnGuide, false)) {
            GuideSpUtil.putBoolean(this, GuideConstantValue.isShowRegisteVpnGuide, true);
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(ivWallet)
                    .setAlpha(150)
                    .setHighTargetCorner(20)
                    .setHighTargetPadding((int) getResources().getDimension(R.dimen.x13))
                    .setHighTargetGraphStyle(Component.CIRCLE)
                    .setOverlayTarget(false)
                    .setOutsideTouchable(false);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                }

                @Override
                public void onDismiss() {
                    EventBus.getDefault().post(new ShowGuide(2));
                }
            });

            builder.addComponent(new RegistVpnComponent());
            Guide guide = builder.createGuide();
            guide.setShouldCheckLocInWindow(false);
            guide.show(this);
        } else {
            EventBus.getDefault().post(new ShowGuide(2));
        }
    }

    private void showGuideViewEnterSetting() {
        if (!GuideSpUtil.getBoolean(this, GuideConstantValue.isShowEnterSettingGuide, false)) {
            GuideSpUtil.putBoolean(this, GuideConstantValue.isShowEnterSettingGuide, true);
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(ivWallet)
                    .setAlpha(150)
                    .setHighTargetCorner(20)
                    .setHighTargetPadding((int) getResources().getDimension(R.dimen.x20))
                    .setHighTargetGraphStyle(Component.CIRCLE)
                    .setOverlayTarget(false)
                    .setOutsideTouchable(false);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                }

                @Override
                public void onDismiss() {

                }
            });

            builder.addComponent(new EnterSettingComponent());
            Guide guide = builder.createGuide();
            guide.setShouldCheckLocInWindow(false);
            guide.show(this);
        }
    }

    private void showGuideViewEnterWallet() {
        if (!GuideSpUtil.getBoolean(this, GuideConstantValue.isShowEnterWalletGuide, false)) {
            GuideSpUtil.putBoolean(this, GuideConstantValue.isShowEnterWalletGuide, true);
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(viewWallet)
                    .setAlpha(150)
                    .setHighTargetCorner(20)
                    .setHighTargetPadding(-15)
                    .setHighTargetGraphStyle(Component.CIRCLE)
                    .setOverlayTarget(false)
                    .setOutsideTouchable(false);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {

                }

                @Override
                public void onDismiss() {

                }
            });

            builder.addComponent(new EnterWalletComponent());
            Guide guide = builder.createGuide();
            guide.setShouldCheckLocInWindow(false);
            guide.show(this);
        }
    }

    private void showGuideViewVpnFragment() {
        if (!GuideSpUtil.getBoolean(this, GuideConstantValue.isShowEnterVpnGuide, false)) {
            GuideSpUtil.putBoolean(this, GuideConstantValue.isShowEnterVpnGuide, true);
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(viewVpn)
                    .setAlpha(150)
                    .setHighTargetCorner(20)
                    .setHighTargetPadding(-17)
                    .setHighTargetGraphStyle(Component.CIRCLE)
                    .setOverlayTarget(false)
                    .setOutsideTouchable(false);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {

                }

                @Override
                public void onDismiss() {
                    EventBus.getDefault().post(new ShowGuide(0));
                }
            });

            builder.addComponent(new EnterVpnComponent());
            Guide guide = builder.createGuide();
            guide.setShouldCheckLocInWindow(false);
            guide.show(this);
        }
    }

    public void clearGuide() {
        GuideSpUtil.putBoolean(this, GuideConstantValue.isShowEnterSettingGuide, false);
        GuideSpUtil.putBoolean(this, GuideConstantValue.isShowRegisteVpnGuide, false);
        GuideSpUtil.putBoolean(this, GuideConstantValue.isShowVpnListGuide, false);
        GuideSpUtil.putBoolean(this, GuideConstantValue.isShowSettingGuide, false);
        GuideSpUtil.putBoolean(this, GuideConstantValue.isShowWalletDetailGuide, false);
        GuideSpUtil.putBoolean(this, GuideConstantValue.isShowNewWalletGuide, false);
        GuideSpUtil.putBoolean(this, GuideConstantValue.isShowChooseCountryGuide, false);
        GuideSpUtil.putBoolean(this, GuideConstantValue.isShowConnectVpnGuide, false);
        GuideSpUtil.putBoolean(this, GuideConstantValue.isShowEnterWalletGuide, false);
        GuideSpUtil.putBoolean(this, GuideConstantValue.isShowEnterVpnGuide, false);
        GuideSpUtil.putBoolean(this, GuideConstantValue.isShowUnLockGuide, false);
    }


}