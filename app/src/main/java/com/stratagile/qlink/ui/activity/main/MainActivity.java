package com.stratagile.qlink.ui.activity.main;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.db.BuySellBuyTodo;
import com.stratagile.qlink.db.BuySellSellTodo;
import com.stratagile.qlink.db.EntrustTodo;
import com.stratagile.qlink.db.TopupTodoList;
import com.stratagile.qlink.db.VpnServerRecord;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.AppVersion;
import com.stratagile.qlink.entity.QrEntity;
import com.stratagile.qlink.entity.SwitchToOtc;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.eventbus.ChangeToTestWallet;
import com.stratagile.qlink.entity.eventbus.ChangeViewpager;
import com.stratagile.qlink.entity.eventbus.ChangeWalletNeedRefesh;
import com.stratagile.qlink.entity.eventbus.CheckConnectRsp;
import com.stratagile.qlink.entity.eventbus.ForegroundCallBack;
import com.stratagile.qlink.entity.eventbus.FreeCount;
import com.stratagile.qlink.entity.eventbus.GetPairs;
import com.stratagile.qlink.entity.eventbus.MyStatus;
import com.stratagile.qlink.entity.eventbus.ReCreateMainActivity;
import com.stratagile.qlink.entity.eventbus.ShowBind;
import com.stratagile.qlink.entity.eventbus.ShowGuide;
import com.stratagile.qlink.entity.eventbus.StartFilter;
import com.stratagile.qlink.entity.otc.TradePair;
import com.stratagile.qlink.entity.reward.Dict;
import com.stratagile.qlink.entity.topup.TopupOrderList;
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
import com.stratagile.qlink.ui.activity.main.component.DaggerMainComponent;
import com.stratagile.qlink.ui.activity.main.contract.MainContract;
import com.stratagile.qlink.ui.activity.main.module.MainModule;
import com.stratagile.qlink.ui.activity.main.presenter.MainPresenter;
import com.stratagile.qlink.ui.activity.my.AccountActivity;
import com.stratagile.qlink.ui.activity.my.Login1Fragment;
import com.stratagile.qlink.ui.activity.my.MyFragment;
import com.stratagile.qlink.ui.activity.otc.MarketFragment;
import com.stratagile.qlink.ui.activity.otc.NewOrderActivity;
import com.stratagile.qlink.ui.activity.otc.OtcOrderRecordActivity;
import com.stratagile.qlink.ui.activity.reward.MyClaimActivity;
import com.stratagile.qlink.ui.activity.topup.TopUpFragment;
import com.stratagile.qlink.ui.activity.topup.TopupOrderListActivity;
import com.stratagile.qlink.ui.activity.wallet.AllWalletFragment;
import com.stratagile.qlink.ui.activity.wallet.FreeConnectActivity;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.WalletQRCodeActivity;
import com.stratagile.qlink.ui.adapter.TradePairDecoration;
import com.stratagile.qlink.ui.adapter.otc.TradePairAdapter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.CountDownTimerUtils;
import com.stratagile.qlink.utils.DoubleClickHelper;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.KotlinConvertJavaUtils;
import com.stratagile.qlink.utils.LocalWalletUtil;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.SystemUtil;
import com.stratagile.qlink.utils.TimeUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.utils.VersionUtil;
import com.stratagile.qlink.view.ActiveTogglePopWindow;
import com.stratagile.qlink.view.NoScrollViewPager;
import com.stratagile.qlink.view.SweetAlertDialog;
import com.vondear.rxtools.RxTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.Inflater;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import info.hoang8f.android.segmented.SegmentedGroup;
import qlc.utils.Helper;

//import com.facebook.appevents.AppEventsLogger;

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
    //    @BindView(R.id.tv_free)
//    TextView tvFree;
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
    @BindView(R.id.financeCome)
    ImageView financeCome;
    @BindView(R.id.rlWallet)
    RelativeLayout rlWallet;
    @BindView(R.id.button21)
    RadioButton button21;
    @BindView(R.id.button22)
    RadioButton button22;
    @BindView(R.id.segmentControlView)
    SegmentedGroup segmentControlView;
    @BindView(R.id.xxx)
    View xxx;
    @BindView(R.id.view_all_wallet)
    View viewAllWallet;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.recyclerViewTradePair)
    RecyclerView recyclerViewTradePair;
    @BindView(R.id.tvReset)
    TextView tvReset;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.drawerlayout_side_tv)
    LinearLayout drawerlayoutSideTv;
    private FirebaseAnalytics mFirebaseAnalytics;
    private MainViewModel viewModel;

    public static final int START_NO_WALLLET = 0;
    public static final int START_CREATE_PASSWORD = 2;
    public static final int START_VERTIFY_PASSWORD = 3;
    public static final int START_SELECT_PICTURE = 4;
    public static final int START_QRCODE = 5;
    public static final int START_ADD_TOKEN = 6;
    public static final int START_CHOOSE_WALLET = 7;
    public static final int NEW_ORDER = 8;

    public static MainActivity mainActivity;


    //    DisconnectVpnSuccessBroadReceiver disconnectVpnSuccessBroadReceiver = new DisconnectVpnSuccessBroadReceiver();
    private LocationManager locationManager;

    private MyStatus myStatusFlag;
    private CountDownTimerUtils countDownTimerUtils;

    private CountDownTimerUtils countDownTimerUtilsOnVpnServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "MainActivity");
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainActivity");
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "MainActivity");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
        needFront = true;
        super.onCreate(savedInstanceState);
        mainActivity = this;
        getP2pId();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        SpUtil.putBoolean(this, ConstantValue.showTestFlag, false);
        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(UIUtils.getDisplayWidth(this), UIUtils.getStatusBarHeight(this));
        statusBar.setLayoutParams(llp);
        financeCome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        recyclerViewTradePair.addItemDecoration(new TradePairDecoration(10));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showVpnGuide(ShowGuide showGuide) {
        if (showGuide.getNumber() == 3) {
//            showGuideViewVpnFragment();
        }
    }

    TradePairAdapter tradePairAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startFilter(StartFilter startFilter) {
        recyclerViewTradePair.setLayoutManager(new GridLayoutManager(this, 2));
        ArrayList<TradePair.PairsListBean> arrayList = new ArrayList<>();
        try {
            for (int i = 0; i < viewModel.pairsLiveData.getValue().size(); i++) {
                arrayList.add(viewModel.pairsLiveData.getValue().get(i).clone());
            }
            tradePairAdapter = new TradePairAdapter(arrayList);
            recyclerViewTradePair.setAdapter(tradePairAdapter);
            drawerLayout.openDrawer(GravityCompat.END);
            tradePairAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    tradePairAdapter.getData().get(position).setSelect(!tradePairAdapter.getData().get(position).isSelect());
                    tradePairAdapter.notifyItemChanged(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getP2pId() {
        String p2pId = SpUtil.getString(this, ConstantValue.P2PID, "");
        if ("".equals(p2pId)) {
            p2pId = FileUtil.getLocalP2pId();
            if ("".equals(p2pId)) {
                UUID uuid = UUID.randomUUID();
                String uniqueId = uuid.toString();
                String hex = Helper.byteToHexString(uniqueId.getBytes());
                KLog.i(hex + uniqueId.substring(0, 4));
                p2pId = hex + uniqueId.substring(0, 4);
                SpUtil.putString(this, ConstantValue.P2PID, p2pId);
                FileUtil.saveP2pId2Local(p2pId);
            } else {
                KLog.i(p2pId);
                SpUtil.putString(this, ConstantValue.P2PID, p2pId);
            }
        } else {
            KLog.i(p2pId);
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

    private void getStakeQlcCount() {
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, String> infoMap = new HashMap<>();
                infoMap.put("dictType", "winq_reward_qlc_amount");
                mPresenter.qurryDict(infoMap);
            }
        }, 200);
    }

    private SweetAlertDialog sweetAlertDialog;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showBind(ShowBind showBind) {
        showCanClaimPop(freeQlcStake);
    }

    public void showCanClaimPop(String qlcCount) {
        View view  = getLayoutInflater().inflate(R.layout.alert_can_claim, null, false);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        TextView qlc = view.findViewById(R.id.qlc);
        qlc.setText(qlcCount);
        TextView tvOpreate = view.findViewById(R.id.tvOpreate);
        tvOpreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.dismissWithAnimation();
                if (ConstantValue.currentUser == null) {
                    viewPager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this, AccountActivity.class));
                        }
                    }, 200);
                }
                if (ConstantValue.currentUser != null && ConstantValue.currentUser.getBindDate() != null) {

                }
                if (ConstantValue.currentUser != null && ("".equals(ConstantValue.currentUser.getBindDate()) || ConstantValue.currentUser.getBindDate() == null)) {
                    bindQlcWallet();
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
    }

    private void bindQlcWallet() {
        HashMap<String, String> map = new HashMap<>();
        map.put("account", ConstantValue.currentUser.getAccount());
        map.put("token", AccountUtil.getUserToken());
        mPresenter.bindQlcWallet(map);
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

    @Override
    public void setLastVersion(AppVersion appVersion) {
        String[] version = appVersion.getData().getVersion_number().split("#");
        int lastVersion = Integer.valueOf(version[0]);
        int ignoreVersion = SpUtil.getInt(this, ConstantValue.ignoreVersion, 310);
        int currentVersion = VersionUtil.getAppVersionCode(this);
        KLog.i("currentVersion= " + currentVersion);
        if (lastVersion > currentVersion) {
            if (lastVersion > ignoreVersion) {
                KotlinConvertJavaUtils.INSTANCE.showNewVersionDialog(this, lastVersion);
            }
        }
    }

    @Override
    public void bindSuccess() {
        ConstantValue.currentUser.setBindDate(TimeUtil.getTime());
        AppConfig.getInstance().getDaoSession().getUserAccountDao().update(ConstantValue.currentUser);
        Set<String> tags = new HashSet<>();
        tags.add(ConstantValue.userAll);
        if (!"".equals(ConstantValue.currentUser.getBindDate())) {
            tags.add(ConstantValue.userLend);
        }
        if ("Meizu16th".equals(SystemUtil.getDeviceBrand() + SystemUtil.getSystemModel())) {
            KLog.i("添加测试tag");
            tags.add("qwallet_test");
        }
        ConstantValue.jpushOpreateCount++;
        JPushInterface.setTags(this, ConstantValue.jpushOpreateCount, tags);
        ToastUtil.displayShortToast(getString(R.string.bind_success));
    }

    @Override
    public void setStakeQlc(Dict dict) {
        if (ConstantValue.currentUser == null || ConstantValue.currentUser.getBindDate() == null || "".equals(ConstantValue.currentUser.getBindDate())) {
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showCanClaimPop(dict.getData().getValue());
                    freeQlcStake = dict.getData().getValue();
                }
            }, 200);
        }
    }

    private String freeQlcStake = "1500";
    @Override
    public void reCreateToopupSuccess() {
        handlerTopupTodoList();
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
        mPresenter.getTox();
//        getLocation();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (!SpUtil.getString(this, ConstantValue.P2PID, "").equals("")) {
            Map<String, String> infoMap1 = new HashMap<>();
            infoMap1.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
            mPresenter.zsFreeNum(infoMap1);
        }
        getStakeQlcCount();

        segmentControlView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (viewModel.pairsLiveData.getValue() == null || viewModel.pairsLiveData.getValue().size() == 0) {
                    EventBus.getDefault().post(new GetPairs());
                } else {
                    if (i == R.id.button21) {
                        viewModel.currentEntrustOrderType.postValue(ConstantValue.orderTypeSell);
                    } else {
                        viewModel.currentEntrustOrderType.postValue(ConstantValue.orderTypeBuy);
                    }
                }
            }
        });
        LogUtil.addLog(SystemUtil.getDeviceBrand() + "  " + SystemUtil.getSystemModel() + "   " + SystemUtil.getSystemVersion() + "   " + VersionUtil.getAppVersionName(this), getClass().getSimpleName());
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new TopUpFragment();
                } else if (position == 1) {
                    return new MarketFragment();
                } else if (position == 2) {
                    return new AllWalletFragment();
                } else if (position == 3) {
                    return new MyFragment();
                } else {
                    return new MyFragment();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
        //设置BottomNavigationMenuView的字体
        bottomNavigation.enableAnimation(false);
        bottomNavigation.enableShiftingMode(false);
        bottomNavigation.enableItemShiftingMode(false);
        bottomNavigation.enableAnimation(false);
        viewPager.setOffscreenPageLimit(4);
//        bottomNavigation.setTextSize(12);
//        bottomNavigation.setIconSizeAt(0, 25f, 20.8f);
//        bottomNavigation.setIconSizeAt(1, 25f, 20.8f);
//        bottomNavigation.setIconSizeAt(2, 25f, 20.8f);
//        bottomNavigation.setIconsMarginTop((int) getResources().getDimension(R.dimen.x22));
//        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                resetToDefaultIcon();
                Bundle bundle = new Bundle();
                switch (item.getItemId()) {
                    case R.id.item_sms:
                        item.setIcon(R.mipmap.finance_h);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "MainActivity");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainActivity");
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "MainActivity");
//                        mFirebaseAnalytics.logEvent("finance", bundle);
                        setVpnPage();
                        break;
                    case R.id.item_topup:
                        item.setIcon(R.mipmap.topup_h);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "MainActivity");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainActivity");
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "MainActivity");
//                        mFirebaseAnalytics.logEvent("topup", bundle);
                        setTopupPage();
                        break;
                    case R.id.item_all_wallet:
                        item.setIcon(R.mipmap.wallet_h);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "MainActivity");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainActivity");
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "MainActivity");
//                        mFirebaseAnalytics.logEvent("wallet", bundle);
                        setAllWalletPage();
                        break;
                    case R.id.item_settings:
                        item.setIcon(R.mipmap.settings_h);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "MainActivity");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainActivity");
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "MainActivity");
//                        mFirebaseAnalytics.logEvent("me", bundle);
                        setSettingsPage();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        bottomNavigation.setSelectedItemId(R.id.item_topup);
//        setVpnPage();
//        IntentFilter intent = new IntentFilter();
//        intent.addAction(BroadCastAction.disconnectVpnSuccess);
//        registerReceiver(disconnectVpnSuccessBroadReceiver, intent);
//        startService(new Intent(this, ClientConnectedWifiRecordService.class));
        /**
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
        button21.toggle();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mPresenter.getLastAppVersion();
            }
        }).start();
        viewModel.pairsLiveData.observe(this, new Observer<ArrayList<TradePair.PairsListBean>>() {
            @Override
            public void onChanged(@Nullable ArrayList<TradePair.PairsListBean> pairsListBeans) {
                ArrayList<String> titles = new ArrayList<>();
                for (int i = 0; i < viewModel.pairsLiveData.getValue().size(); i++) {
                    if (!titles.contains(viewModel.pairsLiveData.getValue().get(i).getTradeToken()) && viewModel.pairsLiveData.getValue().get(i).isSelect()) {
                        titles.add(viewModel.pairsLiveData.getValue().get(i).getTradeToken());
                    }
                }
                if (titles.size() == 0) {
                    titles.add(viewModel.pairsLiveData.getValue().get(0).getTradeToken());
                }

            }
        });
        bottomNavigation.postDelayed(() -> handlerTopupTodoList(), 5000);

        bottomNavigation.postDelayed(() -> {
            Bundle bundle = getIntent().getBundleExtra(ConstantValue.EXTRA_BUNDLE);
            if(bundle != null){
                String debit = bundle.getString("skip");
                if (debit != null && !"".equals(debit)) {
                    switch (debit) {
                        case "debit":
                            Intent i = new Intent(this, MyClaimActivity.class);
                            i.putExtras(bundle);
                            startActivity(i);
                            break;
                        case "":
                            break;
                        default:
                            break;
                    }
                }
            }
        }, 2500);

    }

    private void handlerTopupTodoList() {
        KLog.i("开始处理充值的待办事项");
        List<TopupTodoList> topupTodoLists = AppConfig.getInstance().getDaoSession().getTopupTodoListDao().loadAll();
        if (topupTodoLists == null || topupTodoLists.size() == 0) {
            handlerOtcEntrustTodo();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (TopupTodoList topupOrderList : topupTodoLists) {
                        if (!topupOrderList.getCreated()) {
                            reCreateTopupOrder(topupOrderList);
                            return;
                        }
                    }
                }
            }).start();
        }
    }

    private void handlerOtcEntrustTodo() {
        List<EntrustTodo> entrustTodos = AppConfig.getInstance().getDaoSession().getEntrustTodoDao().loadAll();
        if (entrustTodos == null || entrustTodos.size() == 0) {
            handlerBuySellBuyTodoOrder();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (EntrustTodo entrustTodo : entrustTodos) {
                        reCreateEntrustOrder(entrustTodo);
                    }
                }
            }).start();
        }
    }

    private void handlerBuySellBuyTodoOrder() {
        List<BuySellBuyTodo> buySellBuyTodos = AppConfig.getInstance().getDaoSession().getBuySellBuyTodoDao().loadAll();
        if (buySellBuyTodos == null || buySellBuyTodos.size() == 0) {
            handlerBuySellSellTodoOrder();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (BuySellBuyTodo entrustTodo : buySellBuyTodos) {
                        reCreateBuySellBuyOrder(entrustTodo);
                    }
                }
            }).start();
        }
    }

    private void handlerBuySellSellTodoOrder() {
        List<BuySellSellTodo> buySellSellTodos = AppConfig.getInstance().getDaoSession().getBuySellSellTodoDao().loadAll();
        if (buySellSellTodos == null || buySellSellTodos.size() == 0) {

        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (BuySellSellTodo buySellSellTodo : buySellSellTodos) {
                        reCreateBuySellSellOrder(buySellSellTodo);
                    }
                }
            }).start();
        }
    }

    /**
     * 重新生成买卖卖单
     * @param buySellSellTodo
     */
    private void reCreateBuySellSellOrder(BuySellSellTodo buySellSellTodo) {
        if (buySellSellTodo.getTxid() == null || "".equals(buySellSellTodo.getTxid())) {
            AppConfig.getInstance().getDaoSession().getBuySellSellTodoDao().delete(buySellSellTodo);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("account", buySellSellTodo.getAccount());
        map.put("token", buySellSellTodo.getToken());
        map.put("entrustOrderId", buySellSellTodo.getEntrustOrderId());
        map.put("usdtAmount", buySellSellTodo.getUsdtAmount());
        map.put("usdtToAddress", buySellSellTodo.getUsdtToAddress());
        map.put("qgasAmount", buySellSellTodo.getQgasAmount());
        map.put("fromAddress", buySellSellTodo.getFromAddress());
        map.put("txid", buySellSellTodo.getTxid());
        mPresenter.reCreateBuySellSellOrder(map, buySellSellTodo);
    }

    /**
     * 重新生成买卖买单
     * @param buySellBuyTodo
     */
    private void reCreateBuySellBuyOrder(BuySellBuyTodo buySellBuyTodo) {
        if (buySellBuyTodo.getTxid() == null || "".equals(buySellBuyTodo.getTxid())) {
            AppConfig.getInstance().getDaoSession().getBuySellBuyTodoDao().delete(buySellBuyTodo);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("account", buySellBuyTodo.getAccount());
        map.put("token", buySellBuyTodo.getToken());
        map.put("tradeOrderId", buySellBuyTodo.getTradeOrderId());
        map.put("txid", buySellBuyTodo.getTxid());
        mPresenter.reCreateBuySellBuyOrder(map, buySellBuyTodo);
    }

    private void reCreateEntrustOrder(EntrustTodo entrustTodo) {
        if (entrustTodo.getTxid() == null || "".equals(entrustTodo.getTxid())) {
            AppConfig.getInstance().getDaoSession().getEntrustTodoDao().delete(entrustTodo);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("account", entrustTodo.getAccount());
        map.put("token", entrustTodo.getToken());
        if (ConstantValue.currentUser != null) {
            map.put("token", AccountUtil.getUserToken());
        }
        map.put("pairsId", entrustTodo.getPairsId());
        map.put("type", entrustTodo.getType());
        map.put("unitPrice", entrustTodo.getUnitPrice());
        map.put("totalAmount", entrustTodo.getTotalAmount());
        map.put("minAmount", entrustTodo.getMinAmount());
        map.put("maxAmount", entrustTodo.getMaxAmount());
        map.put("qgasAddress", entrustTodo.getQgasAddress());
        map.put("usdtAddress", entrustTodo.getUsdtAddress());
        map.put("fromAddress", entrustTodo.getFromAddress());
        map.put("txid", entrustTodo.getTxid());
        mPresenter.reCreateEntrustOrder(map, entrustTodo);
    }

    private void reCreateTopupOrder(TopupTodoList topupTodoList) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("account", topupTodoList.getAccount());
        infoMap.put("p2pId", topupTodoList.getP2pId());
        infoMap.put("productId", topupTodoList.getProductId());
        infoMap.put("areaCode", topupTodoList.getAreaCode());
        infoMap.put("phoneNumber", topupTodoList.getPhoneNumber());
        infoMap.put("amount", topupTodoList.getAmount());
        infoMap.put("txid", topupTodoList.getTxid());
        infoMap.put("payTokenId", topupTodoList.getPayTokenId());
        mPresenter.reCreateTopupOrder(infoMap, topupTodoList);
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

    private void setTopupPage() {
        financeCome.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//设置状态栏黑色字体
        }
        viewPager.setCurrentItem(0, false);
        tvTitle.setVisibility(View.VISIBLE);
        segmentControlView.setVisibility(View.GONE);
        ivWallet.setVisibility(View.GONE);
        tvTitle.setText(R.string.top_up);
        ivQRCode.setVisibility(View.GONE);
        tvTitle.setTextColor(getResources().getColor(R.color.white));
        statusBar.setBackground(getResources().getDrawable(R.drawable.main_bg_shape));
        rl1.setBackground(getResources().getDrawable(R.drawable.main_bg_shape));
        ivAvater.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(R.mipmap.add_j)
                .into(ivWallet);
        Glide.with(this)
                .load(R.mipmap.icon_protfolio_more)
                .into(ivAvater);
    }

    /**
     * 设置为vpn界面
     */
    private void setVpnPage() {
        financeCome.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//设置状态栏黑色字体
        }
        viewPager.setCurrentItem(1, false);
        tvTitle.setVisibility(View.GONE);
        segmentControlView.setVisibility(View.VISIBLE);
        ivWallet.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.finance);
        ivQRCode.setVisibility(View.GONE);
        tvTitle.setTextColor(getResources().getColor(R.color.white));
        statusBar.setBackground(getResources().getDrawable(R.drawable.main_bg_shape));
        rl1.setBackground(getResources().getDrawable(R.drawable.main_bg_shape));
        ivAvater.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(R.mipmap.add_j)
                .into(ivWallet);
        Glide.with(this)
                .load(R.mipmap.icon_protfolio_more)
                .into(ivAvater);
        if (viewModel.pairsLiveData.getValue() == null || viewModel.pairsLiveData.getValue().size() == 0) {
            EventBus.getDefault().post(new GetPairs());
        }
        if (ConstantValue.currentUser == null) {
            viewModel.noUserLogin.postValue("noUser");
        }
    }

    private void setAllWalletPage() {
//        if (Calendar.getInstance().getTimeInMillis() - dangqianshijian <= jianjushijian) {
//            return;
//        }
        financeCome.setVisibility(View.GONE);
        dangqianshijian = Calendar.getInstance().getTimeInMillis();
        KLog.i("进入钱包页面。。");
        ivWallet.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        segmentControlView.setVisibility(View.GONE);

        if (ConstantValue.isShouldShowVertifyPassword) {
            Intent intent = new Intent(this, VerifyWalletPasswordActivity.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
            bottomNavigation.setSelectedItemId(R.id.item_topup);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//设置状态栏黑色字体
            }
            viewPager.setCurrentItem(2, false);
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
        financeCome.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
        }
        ivWallet.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        segmentControlView.setVisibility(View.GONE);
        ivQRCode.setVisibility(View.GONE);
        viewPager.setCurrentItem(3, false);
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
                        .load(MainAPI.MainBASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
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
        if (requestCode == NEW_ORDER) {
            viewModel.timeStampLiveData.postValue(System.currentTimeMillis());
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
            return;
        }
        if (DoubleClickHelper.isDoubleClick()) {
            finish();
        } else {
            ToastUtil.displayShortToast(getString(R.string.double_click_to_exit_app));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchToOtc(SwitchToOtc switchToOtc) {
        bottomNavigation.setSelectedItemId(R.id.item_sms);
        setVpnPage();
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
//        BitUtil.closedWallet();
//        Intent intent = new Intent();
//        intent.setAction(BroadCastAction.disconnectVpn);
//        sendBroadcast(intent);
        EventBus.getDefault().unregister(this);
//        unregisterReceiver(disconnectVpnSuccessBroadReceiver);
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

    @SuppressLint("RestrictedApi")
    @OnClick({R.id.iv_avater, R.id.rlWallet, R.id.tv_title, R.id.view_wallet, R.id.view_vpn, R.id.ivQRCode, R.id.tvReset, R.id.tvConfirm, R.id.drawerlayout_side_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avater:
                if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
                    if (ConstantValue.currentUser == null) {
                        startActivity(new Intent(this, AccountActivity.class));
                        return;
                    }

                    Intent intent1 = new Intent(this, OtcOrderRecordActivity.class);
                    startActivity(intent1);
                } else if (bottomNavigation.getSelectedItemId() == R.id.item_all_wallet) {
                    if (viewModel.walletTypeMutableLiveData.getValue() == AllWallet.WalletType.EthWallet) {
                        QrEntity qrEntity = new QrEntity(viewModel.allWalletMutableLiveData.getValue().getEthWallet().getAddress(), getString(R.string.eth_receivable_address), "eth", 2);
                        startActivity(new Intent(this, WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                    } else if (viewModel.walletTypeMutableLiveData.getValue() == AllWallet.WalletType.NeoWallet) {
                        QrEntity qrEntity = new QrEntity(viewModel.allWalletMutableLiveData.getValue().getWallet().getAddress(), getString(R.string.neo_receivable_address), "neo", 1);
                        startActivity(new Intent(this, WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                    } else if (viewModel.walletTypeMutableLiveData.getValue() == AllWallet.WalletType.EosWallet) {
                        QrEntity qrEntity = new QrEntity(viewModel.allWalletMutableLiveData.getValue().getEosAccount().getAccountName(), getString(R.string.eos_receivable_address), "eos", 3);
                        startActivity(new Intent(this, WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                    } else if (viewModel.walletTypeMutableLiveData.getValue() == AllWallet.WalletType.QlcWallet) {
                        QrEntity qrEntity = new QrEntity(viewModel.allWalletMutableLiveData.getValue().getQlcAccount().getAddress(), getString(R.string.receivable_address), "qlc", 4);
                        startActivity(new Intent(this, WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                    }
                } else if (bottomNavigation.getSelectedItemId() == R.id.item_topup) {
                    Intent intent1 = new Intent(this, TopupOrderListActivity.class);
                    startActivity(intent1);
                }
                break;
            case R.id.rlWallet:
//                if (bottomNavigation.getSelectedItemId() == R.id.item_market) {
//                    startActivityForResult(new Intent(this, AddTokenActivity.class), START_ADD_TOKEN);
//                    return;
//                }
                if (bottomNavigation.getSelectedItemId() == R.id.item_sms) {
                    if (ConstantValue.currentUser == null) {
                        startActivity(new Intent(this, AccountActivity.class));
                        return;
                    }
//                    if (!"KYC_SUCCESS".equals(ConstantValue.currentUser.getVstatus())) {
//                        KotlinConvertJavaUtils.INSTANCE.needVerify(this);
//                        return;
//                    }
                    startActivityForResult(new Intent(this, NewOrderActivity.class), NEW_ORDER);
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
            case R.id.drawerlayout_side_tv:
                break;
            case R.id.tvReset:
                for (int i = 0; i < tradePairAdapter.getData().size(); i++) {
                    tradePairAdapter.getData().get(i).setSelect(false);
                }
                tradePairAdapter.notifyDataSetChanged();
                break;
            case R.id.tvConfirm:
                boolean hasSelectedTradePair = false;
                ArrayList<TradePair.PairsListBean> pairsListBeans = new ArrayList<>();
                for (int i = 0; i < tradePairAdapter.getData().size(); i++) {
                    if (tradePairAdapter.getData().get(i).isSelect()) {
                        hasSelectedTradePair = true;
                        pairsListBeans.add(tradePairAdapter.getData().get(i));
                    }
                }
                if (hasSelectedTradePair) {
                    viewModel.pairsLiveData.postValue((ArrayList<TradePair.PairsListBean>) tradePairAdapter.getData());
                    drawerLayout.closeDrawer(GravityCompat.END);
                    if (pairsListBeans.size() > 0) {
                        String saveData = new Gson().toJson(pairsListBeans);
                        FileUtil.savaData("/Qwallet/tradePair.json", saveData);
                    }
                 } else {
                    ToastUtil.displayShortToast(getString(R.string.choose_at_least_one_trading_pair));
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

//    public class DisconnectVpnSuccessBroadReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            KLog.i("断开vpn成功， 开始更改ui");
//            if (intent.getAction().equals(BroadCastAction.disconnectVpnSuccess)) {
//                List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
//                for (VpnEntity vpnEntity : vpnEntityList) {
//                    if (vpnEntity.getIsConnected()) {
//                        vpnEntity.setIsConnected(false);
//                        AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
//                    }
//                }
//                EventBus.getDefault().post(new DisconnectVpn());
//            }
//        }
//    }

    @Override
    public void onCreatWalletSuccess(ArrayList<Wallet> importWalletResult, int flag) {
        //AppConfig.getInstance().getDaoSession().getWalletDao().insertInTx(importWalletResult);
        String index = FileUtil.readData("/Qwallet/Address/index.txt");
        if (!"".equals(index) && Integer.valueOf(index) < importWalletResult.size()) {
            SpUtil.putInt(this, ConstantValue.currentWallet, Integer.valueOf(index));
        } else {
            SpUtil.putInt(this, ConstantValue.currentWallet, 0);
            FileUtil.savaData("/Qwallet/Address/index.txt", "0");
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
                        .load(MainAPI.MainBASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
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