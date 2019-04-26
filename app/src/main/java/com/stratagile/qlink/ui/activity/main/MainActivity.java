package com.stratagile.qlink.ui.activity.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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
import android.widget.Button;
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
import com.stratagile.qlink.blockchain.btc.BitUtil;
import com.stratagile.qlink.constant.BroadCastAction;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.VpnServerRecord;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.ContinentAndCountry;
import com.stratagile.qlink.entity.QrEntity;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.eventbus.ChangeToTestWallet;
import com.stratagile.qlink.entity.eventbus.ChangeViewpager;
import com.stratagile.qlink.entity.eventbus.ChangeWalletNeedRefesh;
import com.stratagile.qlink.entity.eventbus.CheckConnectRsp;
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
import com.stratagile.qlink.qlink.P2PCallBack;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.finance.FinanceFragment;
import com.stratagile.qlink.ui.activity.finance.MyProductActivity;
import com.stratagile.qlink.ui.activity.main.component.DaggerMainComponent;
import com.stratagile.qlink.ui.activity.main.contract.MainContract;
import com.stratagile.qlink.ui.activity.main.module.MainModule;
import com.stratagile.qlink.ui.activity.main.presenter.MainPresenter;
import com.stratagile.qlink.ui.activity.my.AccountActivity;
import com.stratagile.qlink.ui.activity.my.MyFragment;
import com.stratagile.qlink.ui.activity.sms.SmsFragment;
import com.stratagile.qlink.ui.activity.wallet.AllWalletFragment;
import com.stratagile.qlink.ui.activity.wallet.CreateWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.FreeConnectActivity;
import com.stratagile.qlink.ui.activity.wallet.ProfilePictureActivity;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.WalletFragment;
import com.stratagile.qlink.ui.activity.wallet.WalletQRCodeActivity;
import com.stratagile.qlink.utils.CountDownTimerUtils;
import com.stratagile.qlink.utils.DoubleClickHelper;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.LocalWalletUtil;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.SpringAnimationUtil;
import com.stratagile.qlink.utils.SystemUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.utils.VersionUtil;
import com.stratagile.qlink.view.ActiveTogglePopWindow;
import com.stratagile.qlink.view.BottomNavigationViewEx;
import com.stratagile.qlink.view.NoScrollViewPager;
import com.stratagile.qlink.view.SweetAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class MainActivity extends BaseActivity implements MainContract.View, ActiveTogglePopWindow.OnItemClickListener {

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
    @BindView(R.id.tv_free)
    TextView tvFree;
    @BindView(R.id.iv_wallet)
    ImageView ivWallet;
    @BindView(R.id.view_vpn)
    View viewVpn;
    @BindView(R.id.view_wallet)
    View viewWallet;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.rl2)
    RelativeLayout rl2;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    private FirebaseAnalytics mFirebaseAnalytics;
    private MainViewModel viewModel;

    public static final int START_NO_WALLLET = 0;
    public static final int START_CREATE_PASSWORD = 2;
    public static final int START_VERTIFY_PASSWORD = 3;
    public static final int START_SELECT_PICTURE = 4;
    public static final int START_QRCODE = 5;
    public static final int START_ADD_TOKEN = 6;
    public static final int START_CHOOSE_WALLET = 7;

    public static MainActivity mainActivity;

    DisconnectVpnSuccessBroadReceiver disconnectVpnSuccessBroadReceiver = new DisconnectVpnSuccessBroadReceiver();
    private LocationManager locationManager;

    private MyStatus myStatusFlag;
    private CountDownTimerUtils countDownTimerUtils;

    private CountDownTimerUtils countDownTimerUtilsOnVpnServer;

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
        SpUtil.putBoolean(this, ConstantValue.showTestFlag, false);
        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(UIUtils.getDisplayWidth(this), UIUtils.getStatusBarHeight(this));
        statusBar.setLayoutParams(llp);
        if (!SpUtil.getString(this, ConstantValue.myAvatarPath, "").equals("")) {
            if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
                Glide.with(this)
                        .load(MainAPI.MainBASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().optionsMainColor)
                        .into(ivAvater);
            } else {
                Glide.with(this)
                        .load(API.BASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().optionsMainColor)
                        .into(ivAvater);
            }
        }
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("MainActivity");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showVpnGuide(ShowGuide showGuide) {
        if (showGuide.getNumber() == 3) {
//            showGuideViewVpnFragment();
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
//        if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
//            Glide.with(this)
//                    .load(R.mipmap.icon_set_active)
//                    .into(ivWallet);
//            ivWallet.setVisibility(View.VISIBLE);
//        }
    }

    int isShowAct = 0;

    @Override
    public void onGetShowActBack(int isShow) {
//        isShowAct = isShow;
//        if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
//            Glide.with(this)
//                    .load(R.mipmap.icon_set_active)
//                    .into(ivWallet);
//            ivWallet.setVisibility(View.VISIBLE);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetFreeNumBack(FreeCount freeCount) {
//        if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
//            Glide.with(this)
//                    .load(R.mipmap.icon_set_active)
//                    .into(ivWallet);
//            ivWallet.setVisibility(View.VISIBLE);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void reCreateMainActivity(ReCreateMainActivity reCreateMainActivity) {
        recreate();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeTestUI(ChangeWalletNeedRefesh changeWalletNeedRefesh) {
//        mPresenter.getMainAddress();
//        Map<String, String> infoMap1 = new HashMap<>();
//        infoMap1.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
//        mPresenter.zsFreeNum(infoMap1);
//        if (!SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
//            if (SpUtil.getBoolean(this, ConstantValue.showTestFlag, true)) {
//                statusBar.setBackgroundColor(getResources().getColor(R.color.color_f51818));
//                statusBar.setText(getString(R.string.testnet));
//            }
//        } else {
//            statusBar.setBackground(getResources().getDrawable(R.drawable.navigation_shape));
//            statusBar.setText("");
//        }
    }

    @Override
    protected void initData() {
        LocalWalletUtil.initGreenDaoFromLocal();
//        if (!SpUtil.getString(this, ConstantValue.walletPassWord, "").equals("")) {
//            if (ConstantValue.isShouldShowVertifyPassword) {
//                Intent intent = new Intent(this, VerifyWalletPasswordActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//            }
//        } else {
//            Intent intent = new Intent(this, CreateWalletPasswordActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//        }
        mPresenter.getTox();
        getLocation();

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (!SpUtil.getString(this, ConstantValue.P2PID, "").equals("")) {
            Map<String, String> infoMap1 = new HashMap<>();
            infoMap1.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
            mPresenter.zsFreeNum(infoMap1);
        }
//        qlinkcom.getP2PConnnectStatus(new P2PCallBack() {
//            @Override
//            public void onResult(String result) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        SpUtil.putString(MainActivity.this, ConstantValue.P2PID, result);
//                        EventBus.getDefault().post(new P2pBack());
//                        String p2pId = result;
//                        String saveResult = FileUtil.saveP2pId2Local(result);
//                        KLog.i("上次的p2pId" + saveResult);
//                        if ("".equals(saveResult)) {
//
//                        } else {
//                            showp2pIdChangeDialog(result, saveResult);
//                        }
//                        if (SpUtil.getString(MainActivity.this, ConstantValue.myAvatarPath, "").equals("")) {
//                            Map<String, String> infoMap = new HashMap<>();
//                            infoMap.put("p2pId", p2pId);
//                            mPresenter.userAvatar(infoMap);
//                        }
//                    }
//                });
//            }
//        });
        LogUtil.addLog(SystemUtil.getDeviceBrand() + "  " + SystemUtil.getSystemModel() + "   " + SystemUtil.getSystemVersion() + "   " + VersionUtil.getAppVersionName(this), getClass().getSimpleName());
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new FinanceFragment();
                } else if (position == 1) {
                    return new AllWalletFragment();
                } else if (position == 2) {
                    return new MyFragment();
                } else {
                    return new MyFragment();
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
        bottomNavigation.setTextSize(12);
//        bottomNavigation.setTypeface(Typeface.createFromAsset(getAssets(), "vagroundedbt.ttf"));
        viewPager.setOffscreenPageLimit(4);
        bottomNavigation.setIconSizeAt(0, 25f, 20.8f);
        bottomNavigation.setIconSizeAt(1, 25f, 20.8f);
        bottomNavigation.setIconSizeAt(2, 25f, 20.8f);
//        bottomNavigation.setIconSizeAt(3, 25f, 20.8f);
        bottomNavigation.setIconsMarginTop((int) getResources().getDimension(R.dimen.x22));
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                resetToDefaultIcon();
                switch (item.getItemId()) {
                    case R.id.item_sms:
                        item.setIcon(R.mipmap.finance_h);
                        setVpnPage();
                        break;
                    case R.id.item_all_wallet:
                        item.setIcon(R.mipmap.wallet_h);
                        setAllWalletPage();
                        break;
                    case R.id.item_settings:
                        item.setIcon(R.mipmap.settings_h);
                        setSettingsPage();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        bottomNavigation.setSelectedItemId(R.id.item_sms);
//        setVpnPage();
        IntentFilter intent = new IntentFilter();
        intent.addAction(BroadCastAction.disconnectVpnSuccess);
        registerReceiver(disconnectVpnSuccessBroadReceiver, intent);
//        startService(new Intent(this, ClientConnectedWifiRecordService.class));
        /**
         * @see WalletFragment#refreshNeo(NeoRefrash)
         */
//        EventBus.getDefault().post(new NeoRefrash());
        //创建neo钱包。为唯一对象，每次在使用的钱包只有一个。
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        for (int i = 0; i < walletList.size(); i++) {
            if (walletList.get(i).getIsCurrent()) {
                Wallet wallet = walletList.get(i);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Account.INSTANCE.getWallet() == null) {
                            Account.INSTANCE.fromWIF(wallet.getWif());
                        }
                    }
                }).start();
            }
        }

//        ivWallet.post(new Runnable() {
//            @Override
//            public void run() {
//                showGuideViewEnterWallet();
//            }
//        });
    }

    private void showp2pIdChangeDialog(String currentP2pId, String lastP2pId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.p2pid_change_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(getString(R.string.currentP2pId) + currentP2pId + "\n\n" + getString(R.string.lastP2pId) + lastP2pId);
        title.setText(R.string.P2pId_is_changed);
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setText(getString(R.string.cancel).toLowerCase());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void resetToDefaultIcon() {
        bottomNavigation.getMenu().findItem(R.id.item_all_wallet).setIcon(R.mipmap.wallet_n);
//        bottomNavigation.getMenu().findItem(R.id.item_market).setIcon(R.mipmap.icon_markets_n);
        bottomNavigation.getMenu().findItem(R.id.item_sms).setIcon(R.mipmap.finance_n);
        bottomNavigation.getMenu().findItem(R.id.item_settings).setIcon(R.mipmap.settings_n);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkSharerConnectRsp(CheckConnectRsp checkConnectRsp) {

        List<VpnServerRecord> vpnServerRecordList = AppConfig.getInstance().getDaoSession().getVpnServerRecordDao().loadAll();
        for (VpnServerRecord vpnServerRecord : vpnServerRecordList) {
            Map<String, Object> infoMap = new HashMap<>();
            infoMap.put("vpnName", vpnServerRecord.getVpnName());
            infoMap.put("vpnfileName", vpnServerRecord.getVpnfileName());
            infoMap.put("userName", vpnServerRecord.getUserName());
            infoMap.put("password", vpnServerRecord.getPassword());
            infoMap.put("privateKey", vpnServerRecord.getPrivateKey());
            QlinkUtil.parseMap2StringAndSend(vpnServerRecord.getP2pId(), ConstantValue.vpnUserPassAndPrivateKeyRsp, infoMap);
            AppConfig.getInstance().getDaoSession().getVpnServerRecordDao().delete(vpnServerRecord);
        }

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
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void changeToTestWallet(ChangeToTestWallet changeToTestWallet) {
//        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
//        Account.INSTANCE.fromWIF(walletList.get(SpUtil.getInt(this, ConstantValue.currentWallet, 0)).getWif());
    }

    /**
     * 设置为vpn界面
     */
    private void setVpnPage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//设置状态栏黑色字体
        }
        viewPager.setCurrentItem(0, false);
        tvTitle.setVisibility(View.VISIBLE);
        tvFree.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.finance);
        ivQRCode.setVisibility(View.GONE);
        tvTitle.setTextColor(getResources().getColor(R.color.white));
        statusBar.setBackground(getResources().getDrawable(R.drawable.main_bg_shape));
        rl1.setBackground(getResources().getDrawable(R.drawable.main_bg_shape));
        ivWallet.setVisibility(View.GONE);
        ivAvater.setVisibility(View.GONE);
        if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
            Glide.with(this)
                    .load(MainAPI.MainBASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                    .apply(AppConfig.getInstance().optionsMainColor)
                    .into(ivAvater);
        } else {
            Glide.with(this)
                    .load(API.BASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                    .apply(AppConfig.getInstance().optionsMainColor)
                    .into(ivAvater);
        }
    }

    private void setAllWalletPage() {
//        if (Calendar.getInstance().getTimeInMillis() - dangqianshijian <= jianjushijian) {
//            return;
//        }
        dangqianshijian = Calendar.getInstance().getTimeInMillis();
        KLog.i("进入钱包页面。。");
        ivWallet.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvFree.setVisibility(View.GONE);

        if (ConstantValue.isShouldShowVertifyPassword) {
            Intent intent = new Intent(this, VerifyWalletPasswordActivity.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
            bottomNavigation.setSelectedItemId(R.id.item_sms);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//设置状态栏黑色字体
            }
            viewPager.setCurrentItem(1, false);
            ivQRCode.setVisibility(View.VISIBLE);
            statusBar.setBackgroundColor(getResources().getColor(R.color.mainColor));
            rl1.setBackgroundColor(getResources().getColor(R.color.mainColor));
            tvTitle.setText(R.string.wallet);
            tvTitle.setTextColor(getResources().getColor(R.color.white));
            Glide.with(this)
                    .load(R.mipmap.add_j)
                    .into(ivWallet);
            ivAvater.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(R.mipmap.qr_code_n)
                    .into(ivAvater);
//                    showGuideViewEnterSetting();
        }
    }

    /**
     * 设置为钱包界面
     */
    int jianjushijian = 500;
    long dangqianshijian = 0;

    private void setSettingsPage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
        }
        ivWallet.setVisibility(View.GONE);
        ivWallet.setClickable(true);
        tvFree.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        ivQRCode.setVisibility(View.GONE);
        viewPager.setCurrentItem(2, false);
        statusBar.setBackgroundColor(getResources().getColor(R.color.white));
        rl1.setBackgroundColor(getResources().getColor(R.color.white));
        tvTitle.setText(R.string.me);
        tvTitle.setTextColor(getResources().getColor(R.color.color_29282a));
        ivAvater.setVisibility(View.GONE);
        Glide.with(this)
                .load(R.mipmap.icon_set1)
                .into(ivWallet);
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
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ConstantValue.isShouldShowVertifyPassword = false;
                KLog.i("验证指纹回来，并且成功");
                bottomNavigation.setSelectedItemId(R.id.item_all_wallet);
            } else {
                bottomNavigation.setSelectedItemId(R.id.item_sms);
            }

        }
        if (requestCode == START_SELECT_PICTURE && resultCode == RESULT_OK) {
//            File dataFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + SpUtil.getString(this, ConstantValue.myAvaterUpdateTime, "") + ".jpg", "");
            if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
                Glide.with(this)
                        .load(MainAPI.MainBASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().optionsMainColor)
                        .into(ivAvater);
            } else {
                Glide.with(this)
                        .load(API.BASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().optionsMainColor)
                        .into(ivAvater);
            }
        }
        if (requestCode == START_QRCODE && resultCode == RESULT_OK) {
            viewModel.qrcode.postValue(data.getStringExtra("result"));
        }
        if (requestCode == START_CHOOSE_WALLET && resultCode == RESULT_OK) {
            viewModel.timeStampAllWalletInitData.postValue(Calendar.getInstance().getTimeInMillis());
        }
        if (requestCode == START_ADD_TOKEN) {
            viewModel.timeStampLiveData.postValue(Calendar.getInstance().getTimeInMillis());
        }
    }

    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isDoubleClick()) {
            finish();
        } else {
            ToastUtil.displayShortToast("Double click to exit app");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleToAppToBack(ForegroundCallBack foregroundCallBack) {
        if (!foregroundCallBack.isForeground()) {
            ConstantValue.isShouldShowVertifyPassword = true;
            bottomNavigation.setSelectedItemId(R.id.item_sms);
            setVpnPage();
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
        KLog.i("mainactivity关闭");
        BitUtil.closedWallet();
        Intent intent = new Intent();
        intent.setAction(BroadCastAction.disconnectVpn);
        sendBroadcast(intent);
        EventBus.getDefault().unregister(this);
        unregisterReceiver(disconnectVpnSuccessBroadReceiver);
        ActivityManager activityMgr = (ActivityManager) AppConfig.getInstance()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityMgr.killBackgroundProcesses(AppConfig.getInstance().getPackageName());
        Process.killProcess(Process.myPid());
        System.exit(0);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setViewPager(ChangeViewpager changeViewpager) {
        switch (changeViewpager.getPosition()) {
            case 0:
                bottomNavigation.setSelectedItemId(R.id.item_sms);
                break;
            case 1:
                bottomNavigation.setSelectedItemId(R.id.item_all_wallet);
                break;
            case 2:
                bottomNavigation.setSelectedItemId(R.id.item_settings);
                break;
            case 3:
                bottomNavigation.setSelectedItemId(R.id.item_settings);
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
    @OnClick({R.id.iv_avater, R.id.iv_wallet, R.id.tv_title, R.id.view_wallet, R.id.view_vpn, R.id.ivQRCode, R.id.tv_free})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avater:
                if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
                    Intent intent1 = new Intent(this, ProfilePictureActivity.class);
//                Intent intent1 = new Intent(this, SelectWalletTypeActivity.class);
                    startActivityForResult(intent1, START_SELECT_PICTURE);
                } else if (bottomNavigation.getSelectedItemId() == R.id.item_all_wallet) {
                    if (viewModel.walletTypeMutableLiveData.getValue() == AllWallet.WalletType.EthWallet) {
                        QrEntity qrEntity = new QrEntity(viewModel.allWalletMutableLiveData.getValue().getEthWallet().getAddress(), "ETH Address", "eth");
                        startActivity(new Intent(this, WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                    } else if (viewModel.walletTypeMutableLiveData.getValue() == AllWallet.WalletType.NeoWallet) {
                        QrEntity qrEntity = new QrEntity(viewModel.allWalletMutableLiveData.getValue().getWallet().getAddress(), "NEO Address", "neo");
                        startActivity(new Intent(this, WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                    } else if (viewModel.walletTypeMutableLiveData.getValue() == AllWallet.WalletType.EosWallet) {
                        QrEntity qrEntity = new QrEntity(viewModel.allWalletMutableLiveData.getValue().getEosAccount().getAccountName(), "Eos Address", "eos");
                        startActivity(new Intent(this, WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                    }
                }
                break;
            case R.id.iv_wallet:
//                if (bottomNavigation.getSelectedItemId() == R.id.item_market) {
//                    startActivityForResult(new Intent(this, AddTokenActivity.class), START_ADD_TOKEN);
//                    return;
//                }
                if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
                    ActiveTogglePopWindow morePopWindow = new ActiveTogglePopWindow(this);
                    morePopWindow.setOnItemClickListener(MainActivity.this);
                    morePopWindow.showPopupWindow(ivWallet);
                    return;
                }
                if (bottomNavigation.getSelectedItemId() == R.id.item_all_wallet) {
                    startActivityForResult(new Intent(this, SelectWalletTypeActivity.class), START_CHOOSE_WALLET);
                    overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                }
                break;
            case R.id.tv_title:
                if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
                    startActivity(new Intent(this, LogActivity.class));
                }
                break;
            case R.id.view_wallet:
                bottomNavigation.setSelectedItemId(R.id.item_settings);
                break;
            case R.id.view_vpn:
                bottomNavigation.setSelectedItemId(R.id.item_sms);
                break;
            case R.id.ivQRCode:
                startActivityForResult(new Intent(this, ScanQrCodeActivity.class), START_QRCODE);
                break;
            case R.id.tv_free:
                if (ConstantValue.currentUser == null) {
                    startActivity(new Intent(this, AccountActivity.class));
                } else {
                    startActivity(new Intent(this, MyProductActivity.class));
                }
                break;
            default:
                break;
        }
    }

    private void showTestDialog() {
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_tip, null, false);
        TextView tvContent = view.findViewById(R.id.tvContent);
        ImageView imageView = view.findViewById(R.id.ivTitle);
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.op_success));
        tvContent.setText("setting success");
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
        ivWallet.postDelayed(new Runnable() {
            @Override
            public void run() {
                sweetAlertDialog.cancel();
            }
        }, 2000);
    }

    @Override
    public void onItemClick(int id) {
        switch (id) {
            case R.id.rl_detail:
                startActivity(new Intent(this, FreeConnectActivity.class));
                break;
            case R.id.rl_rank:
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
        if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
            if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
                Glide.with(this)
                        .load(MainAPI.MainBASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().optionsMainColor)
                        .into(ivAvater);
            } else {
                Glide.with(this)
                        .load(API.BASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().optionsMainColor)
                        .into(ivAvater);
            }
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
//        org.consenlabs.tokencore.wallet.Wallet wallet = new org.consenlabs.tokencore.wallet.Wallet();
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