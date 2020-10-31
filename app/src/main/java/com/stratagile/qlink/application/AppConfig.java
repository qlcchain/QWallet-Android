package com.stratagile.qlink.application;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
//import com.facebook.FacebookSdk;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.socks.library.KLog;
//import com.squareup.leakcanary.LeakCanary;
import com.stratagile.qlink.BuildConfig;
import com.stratagile.qlink.R;
import com.stratagile.qlink.blockchain.btc.BitUtil;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.constant.MainConstant;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.db.DaoMaster;
import com.stratagile.qlink.db.DaoSession;
import com.stratagile.qlink.db.MySQLiteOpenHelper;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.CurrencyBean;
import com.stratagile.qlink.entity.EosKeyAccount;
import com.stratagile.qlink.entity.eventbus.ForegroundCallBack;
import com.stratagile.qlink.entity.eventbus.OnAppResume;
import com.stratagile.qlink.entity.otc.TradePair;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.ui.activity.main.MainActivity;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.GlideCircleTransform;
import com.stratagile.qlink.utils.GlideCircleTransformMainColor;
import com.stratagile.qlink.utils.GlideCircleTransformWhiteColor;
import com.stratagile.qlink.utils.GlideRoundTransform;
import com.stratagile.qlink.utils.NickUtil;
import com.stratagile.qlink.utils.NotificationUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.VersionUtil;
import com.stratagile.qlink.utils.eth.AppFilePath;
import com.stratagile.qlink.view.AndroidUtilities;
import com.tencent.bugly.crashreport.CrashReport;
import com.today.step.lib.TodayStep;
import com.vondear.rxtools.RxDataTool;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxTool;

import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//import com.facebook.appevents.AppEventsLogger;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

import static com.tencent.bugly.Bugly.applicationContext;

//import com.tencent.bugly.crashreport.CrashReport;

/**
 * 作者：Android on 2017/8/1
 * 邮箱：365941593@qq.com
 * 描述：
 */
public class AppConfig extends MultiDexApplication {
    public static AppConfig instance;
    private AppComponent mAppComponent;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    public TradePair.PairsListBean pair;
    public boolean isMainNet = true;

    private PackageInfo info;
    public Application deviceStorage;
//    public FirebaseRemoteConfig remoteConfig;
    public Handler handler;

    public WalletAppKit walletAppKit;

    public PackageInfo getInfo() {
        return info;
    }
    //    public AppActivityManager mAppActivityManager;

    public static final String MI_PUSH_APP_ID = "2882303761517798507";
    public static final String MI_PUSH_APP_KEY = "5231779840507";

    public static Handler applicationHandler;

    public static VpnEntity currentUseVpn;
    /**
     * 当前vpn的使用类型
     * 0， 为不收费，
     * 1， 收费
     */
    public static int currentVpnUseType = 0;

    public RequestOptions options = new RequestOptions()
            .centerCrop()
            .transform(new GlideCircleTransform())
            .placeholder(R.mipmap.icon_user_default)
            .error(R.mipmap.icon_user_default)
            .priority(Priority.HIGH);

    public RequestOptions optionsTopup = new RequestOptions()
            .centerCrop()
            .transform(new GlideRoundTransform(this, 8))
            .placeholder(R.mipmap.guangdong_mobile)
            .error(R.mipmap.guangdong_mobile)
            .priority(Priority.HIGH);

    public RequestOptions optionsMainColor = new RequestOptions()
            .centerCrop()
            .transform(new GlideCircleTransformMainColor(this))
            .placeholder(R.mipmap.icon_user_default)
            .error(R.mipmap.icon_user_default)
            .priority(Priority.HIGH);

    public RequestOptions optionsWhiteColor = new RequestOptions()
            .centerCrop()
            .transform(new GlideCircleTransformWhiteColor(this))
            .placeholder(R.mipmap.icon_user_default)
            .error(R.mipmap.icon_user_default)
            .priority(Priority.HIGH);

    public RequestOptions optionsAvater = new RequestOptions()
            .centerCrop()
            .transform(new GlideCircleTransformMainColor(this))
            .placeholder(R.mipmap.icon_user_default)
            .error(R.mipmap.icon_user_default)
            .priority(Priority.HIGH);
    public RequestOptions optionsAppeal = new RequestOptions()
            .centerCrop()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .priority(Priority.HIGH);
    public RequestOptions optionsNormal = new RequestOptions()
            .centerCrop()
            .priority(Priority.HIGH);

    public AppConfig() {

    }

    private void initJpush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void initStakeConfig() {
//        isMainNet = SpUtil.getBoolean(this, ConstantValue.isMainNet, true);
        // 还要改html中的正式节点和测试节点
        // html中的smartContractScript
        // 新建抵押查neo链的txid直接跳过了，正式服要改过来
        // 查测试服的qlc余额的方法改了，要在正式服改回来
        // 赎回的时候，要用测试网的unlock方法
        if (isMainNet) {
            //qlc正式网节点
            ConstantValue.qlcNode = "http://wrpc.qlcchain.org:9735";
            //qlc链抵押正式节点
            ConstantValue.qlcStakeNode = "https://nep5.qlcchain.online";
            //neo链正式网查交易记录
            ConstantValue.neoTranscationNode = "https://api.neoscan.io/api/main_net/v1/get_transaction/";
            //创建多重签名地址的hash正式服
            ConstantValue.createMultiSigHash = "02c6e68c61480003ed163f72b41cbb50ded29d79e513fd299d2cb844318b1b8ad5";

            ConstantValue.ethNodeUrl = "https://mainnet.infura.io/v3/dc2243ed5aa5488d9fcf794149f56fc2";
            ConstantValue.currentChainId = 1;
            ConstantValue.qlchash = "0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5";
            ConstantValue.qlcHubEndPoint = "https://hub-test.qlcchain.online";
            ConstantValue.neoNode = "http://seed2.ngd.network:10332";
        } else {
            //qlc测试网节点
//            ConstantValue.qlcNode = "http://rpc-test.qlcchain.online";
            ConstantValue.qlcNode = "http://wrpc.qlcchain.org:9735";
            //qlc抵押测试节点
//            ConstantValue.qlcStakeNode = "http://47.103.54.171:19740";
            //neo测试网查交易记录
            ConstantValue.neoTranscationNode = "https://api.neoscan.io/api/test_net/v1/get_transaction/";
            //创建多重签名地址的hash测试服
            ConstantValue.createMultiSigHash = "0292a55eb2f213d087d71cf0e2e4b047762b6eccc6a6993d7bbea39e7379661afb";
            ConstantValue.ethNodeUrl = "https://rinkeby.infura.io/v3/dc2243ed5aa5488d9fcf794149f56fc2";
            ConstantValue.currentChainId = 4;
            ConstantValue.qlchash = "b9d7ea3062e6aeeb3e8ad9548220c4ba1361d263";
            ConstantValue.qlcHubEndPoint = "https://hub-test.qlcchain.online";
            ConstantValue.neoNode = "http://seed2.ngd.network:20332";
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        KLog.i("设置屏幕");
        try {
//            LocaleController.getInstance().onDeviceConfigurationChange(newConfig);
            AndroidUtilities.checkDisplaySize(applicationContext, newConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtil.removeAllImageAvater(this);
        instance = this;
        KLog.init(BuildConfig.LOG_DEBUG);
        KLog.i("app启动！！！");
        initJpush();
        CrashReport.initCrashReport(this, "2d19fdd0a6", BuildConfig.LOG_DEBUG);
        setupApplicationComponent();
        setDatabase();
        Qsdk.init();
        RxTool.init(this);
        ToastUtil.init();
        initMoney();
        AppFilePath.init(this);
        NickUtil.initUserNickName(this);
        initDbUpdate();
        initResumeListener();
        initStakeConfig();
        setLanguage(false);
        TodayStep.init(MainConstant.MainAppid, MainConstant.unKownKeyButImportant);
        applicationHandler = new Handler(getMainLooper());
        info = getPackageInfo(getPackageName());
        handler = new Handler(Looper.getMainLooper());
        updateNotificationChannels();
        initGlide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(this);
            if (!"com.stratagile.qwallet".equals(processName)){//判断不等于默认进程名称
                WebView.setDataDirectorySuffix(processName);
            } else {
                new WebView(this).destroy();
            }
        }
        AndroidUtilities.checkDisplaySize(this, null);
        //初始化btc钱包
//        org.bitcoinj.core.Context.enableStrictMode();
//        org.bitcoinj.core.Context.propagate(new org.bitcoinj.core.Context(TestNet3Params.get()));
//        KLog.i(org.bitcoinj.core.Context.get().getFeePerKb().toFriendlyString());
//        BitUtil.getWalletKit(this);
//        remoteConfig = FirebaseRemoteConfig.getInstance();
//        adb shell setprop debug.firebase.analytics.app <com.stratagile.qwallet>
//        adb shell setprop debug.firebase.analytics.app com.stratagile.qwallet
        // adb shell setprop debug.firebase.analytics.app .none.
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    private void initGlide() {
        GlideBuilder glideBuilder = new GlideBuilder();
        glideBuilder.setLogLevel(Log.ERROR);
        Glide.init(this, glideBuilder);
    }

    public  String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    public String getBaseUrl() {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, true)) {
            return MainAPI.MainBASE_URL;
        } else {
            return API.BASE_URL;
        }
    }

    public void saveLog(String mode, String operation, String log) {
        HashMap<String, String> infoMap = new HashMap<>();
        infoMap.put("appName", "QWallet");
        infoMap.put("version", VersionUtil.getAppVersionCode(this) + "");
        infoMap.put("os", "Android");
        infoMap.put("deviceModel", RxDeviceTool.getBuildMANUFACTURER() + " " + RxDeviceTool.getBuildBrandModel());
        infoMap.put("mode", mode);
        infoMap.put("operation", operation);
        infoMap.put("happenDate", (System.currentTimeMillis() / 1000) + "");
        infoMap.put("log", log);
        mAppComponent.getHttpApiWrapper().saveLog(infoMap).subscribe(new Consumer<BaseBack>() {
            @Override
            public void accept(BaseBack baseBack) throws Exception {
                //isSuccesse
                KLog.i("onSuccesse");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //onError
                KLog.i("onError");
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //onComplete
                KLog.i("onComplete");
            }
        });
    }


    private void updateNotificationChannels() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager nm = getSystemService(NotificationManager.class);
            ArrayList<NotificationChannel> list = new ArrayList<>();
            list.add(new NotificationChannel("service-vpn", getText(R.string.service_vpn),NotificationManager.IMPORTANCE_LOW));
            list.add(new NotificationChannel("service-proxy", getText(R.string.service_proxy), NotificationManager.IMPORTANCE_LOW));
            list.add(new NotificationChannel("service-transproxy", getText(R.string.service_transproxy), NotificationManager.IMPORTANCE_LOW));
            nm.createNotificationChannels(list);
            nm.deleteNotificationChannel("service-nat");
        }
    }


    private void initMoney() {
        ArrayList<CurrencyBean> currencyBeans = new ArrayList<>();
        currencyBeans.add(new CurrencyBean("USD", true, "$"));
        currencyBeans.add(new CurrencyBean("CNY", false, "¥"));
        currencyBeans.add(new CurrencyBean("TWD", false, "NT$"));
        //港币
        currencyBeans.add(new CurrencyBean("HKD", false, "HK$"));
        //澳门币
        currencyBeans.add(new CurrencyBean("MOP", false, "MOP$"));
        //欧元
        currencyBeans.add(new CurrencyBean("EUR", false, "€"));
        //卢布，俄罗斯
        currencyBeans.add(new CurrencyBean("RUB", false, "Br"));
        //韩元
        currencyBeans.add(new CurrencyBean("KRW", false, "₩"));
        //菲律宾比索
        currencyBeans.add(new CurrencyBean("PHP", false, "₱"));
        //日本币
        currencyBeans.add(new CurrencyBean("JPY", false, "￥"));
        //泰铢
        currencyBeans.add(new CurrencyBean("THB", false, "฿"));
        //土耳其，里拉
        currencyBeans.add(new CurrencyBean("TRY", false, "₺"));
        //越南
        currencyBeans.add(new CurrencyBean("VND", false, "₫"));
        String currency = SpUtil.getString(this, ConstantValue.currencyUnit, "USD");
        for (CurrencyBean currencyBean : currencyBeans) {
            if (currencyBean.getName().equals(currency)) {
                ConstantValue.currencyBean = currencyBean;
            }
        }
    }

    private void initMiPush() {
        if (shouldInit()) {
            MiPushClient.registerPush(this, MI_PUSH_APP_ID, MI_PUSH_APP_KEY);
        }
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                KLog.i(content + t);
            }

            @Override
            public void log(String content) {
                KLog.i(content);
            }
        };
        Logger.setLogger(this, newLogger);
    }

    private void initResumeListener() {
        ForegroundCallbacks.init(this);
        ForegroundCallbacks.getInstance().addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {
                KLog.i("当前程序切换到前台");
                EventBus.getDefault().post(new OnAppResume());
            }

            @Override
            public void onBecameBackground() {
                KLog.i("当前程序切换到后台");
                if (Calendar.getInstance().getTimeInMillis() - SpUtil.getLong(getApplicationContext(), ConstantValue.unlockTime, 0) >= 300000) {
                    EventBus.getDefault().post(new ForegroundCallBack(false));
                }
            }
        });
    }

    private void initDbUpdate() {
        //MigrationHelper.DEBUG = true; //如果你想查看日志信息，请将DEBUG设置为true
//        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, "test.db",
//                null);
//        daoMaster = new DaoMaster(helper.getWritableDatabase());
    }

    /**
     * 分割 Dex 支持
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized AppConfig getInstance() {
        if (null == instance) {
            instance = new AppConfig();
        }
        return instance;
    }


    public void setupApplicationComponent() {
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .aPIModule(new APIModule(this))
//                .repositoryModlule(new RepositoryModlule())
                .build();
        mAppComponent.inject(this);
    }

    public AppComponent getApplicationComponent() {
        return mAppComponent;
    }

    public void setupNet() {
    }

    /**
     * 设置app的语言
     */
    public void setLanguage(boolean isUpdate) {
        //0，中文， 1英文
        Resources resources = getResources();
        // 获取应用内语言
        final Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (SpUtil.getInt(this, ConstantValue.Language, 0) == 0) {
            configuration.locale = Locale.ENGLISH;
        } else {
            KLog.i("设置为中文");
            configuration.locale = Locale.CHINESE;
        }
        getResources().updateConfiguration(configuration, displayMetrics);
        if (isUpdate) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    /**
     * 设置app的语言中英文互换
     */
    public void setLanguageRevise() {
        if (SpUtil.getInt(this, ConstantValue.Language, 0) == 0) {
            SpUtil.putInt(this, ConstantValue.Language, 1);
        } else {
            SpUtil.putInt(this, ConstantValue.Language, 0);
        }
        setLanguage(true);
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//        mHelper = new DaoMaster.DevOpenHelper(this, "qlink-db", null);
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, "qlink-db",
                null);
        mDaoMaster = new DaoMaster(helper.getWritableDatabase());
//        db = mHelper.getWritableDatabase();
//        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
//        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    /**
     * 收益通知栏
     *
     * @param qlc           收益金额
     * @param activityClass 打开哪个界面
     */
    public void showNotificationChannels(double qlc, final Class<?> activityClass) {
        if (Double.valueOf(qlc) == null) {
            return;
        }
        if (qlc == 0) {
            return;
        }
        //实例化工具类，并且调用接口
        NotificationUtil notify1 = new NotificationUtil(getApplicationContext(), 1);
        Intent intent;
        int smallIcon;
        String ticker;
        String title;
        String content;
        try {
           /* // 根据给定的类名初始化类
            Class catClass = Class.forName(className);
            // 实例化这个类
            Object obj = catClass.newInstance();
            obj.getClass();*/
            intent = new Intent(getApplicationContext(), activityClass);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(),
                    1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            smallIcon = R.drawable.ic_notification;
            ticker = getApplicationContext().getString(R.string.income_qlc);
            title = getApplicationContext().getString(R.string.income_qlc);
            content = getApplicationContext().getString(R.string.income) + " " + RxDataTool.format2Decimals(qlc + "") + " " + getApplicationContext().getString(R.string.qlc);
            Uri soundPath = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.coin);
            notify1.notify_normal_singline(pIntent, smallIcon, ticker, title, content, soundPath, true, false);
        } catch (Exception e) {
        } finally {

        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        if (processInfos != null) {
            for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public PackageInfo getPackageInfo(String packageName) {
        try {
            return getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BroadcastReceiver listenForPackageChanges(boolean onetime, Callback callback) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        BroadcastReceiver result = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {
                    return;
                }
                callback.callback();
                if (true) {
                    AppConfig.instance.unregisterReceiver(this);
                }
            }
        };
        AppConfig.instance.registerReceiver(result, filter);
        return result;
    }

    public interface Callback {
        void callback();
    }

}
