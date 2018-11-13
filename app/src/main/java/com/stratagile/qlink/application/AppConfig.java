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
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.service.autofill.Dataset;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.FacebookSdk;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.socks.library.KLog;
//import com.squareup.leakcanary.LeakCanary;
import com.stratagile.qlink.BuildConfig;
import com.stratagile.qlink.R;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.core.OpenVPNService;
import com.stratagile.qlink.core.PRNGFixes;
import com.stratagile.qlink.core.StatusListener;
import com.stratagile.qlink.db.DaoMaster;
import com.stratagile.qlink.db.DaoSession;
import com.stratagile.qlink.db.MySQLiteOpenHelper;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.CurrencyBean;
import com.stratagile.qlink.entity.eventbus.ForegroundCallBack;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.shadowsocks.bg.BaseService;
import com.stratagile.qlink.shadowsocks.database.Profile;
import com.stratagile.qlink.shadowsocks.database.ProfileManager;
import com.stratagile.qlink.shadowsocks.preference.DataStore;
import com.stratagile.qlink.shadowsocks.utils.Action;
import com.stratagile.qlink.shadowsocks.utils.DeviceStorageApp;
import com.stratagile.qlink.shadowsocks.utils.DirectBoot;
import com.stratagile.qlink.ui.activity.main.MainActivity;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.GlideCircleTransform;
import com.stratagile.qlink.utils.GlideCircleTransformMainColor;
import com.stratagile.qlink.utils.NickUtil;
import com.stratagile.qlink.utils.NotificationUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.AppFilePath;
import com.tencent.bugly.crashreport.CrashReport;
import com.vondear.rxtools.RxDataTool;
import com.vondear.rxtools.RxTool;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.facebook.appevents.AppEventsLogger;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

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
    private StatusListener mStatus;

    private PackageInfo info;
    private Profile currentProfile;
    public Application deviceStorage;
//    public FirebaseRemoteConfig remoteConfig;
    public Handler handler;

    public Profile getCurrentProfile() {
//        DataStore.INSTANCE.setProfileId(2);
        if ((DataStore.INSTANCE.getDirectBootAware())) {
            return DirectBoot.INSTANCE.getDeviceProfile();
        } else {
            try {
                return ProfileManager.INSTANCE.getProfile(DataStore.INSTANCE.getProfileId());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public PackageInfo getInfo() {
        return info;
    }
    //    public AppActivityManager mAppActivityManager;

    public static final String MI_PUSH_APP_ID = "2882303761517798507";
    public static final String MI_PUSH_APP_KEY = "5231779840507";

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
            .placeholder(R.mipmap.img_connected_head_portrait)
            .error(R.mipmap.img_connected_head_portrait)
            .priority(Priority.HIGH);

    public RequestOptions optionsMainColor = new RequestOptions()
            .centerCrop()
            .transform(new GlideCircleTransformMainColor(this))
            .placeholder(R.mipmap.img_connected_head_portrait)
            .error(R.mipmap.img_connected_head_portrait)
            .priority(Priority.HIGH);

    public RequestOptions optionsAvater = new RequestOptions()
            .centerCrop()
            .transform(new GlideCircleTransformMainColor(this))
            .placeholder(R.mipmap.img_connected_head_portrait)
            .error(R.mipmap.img_connected_head_portrait)
            .priority(Priority.HIGH);

    public AppConfig() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtil.removeAllImageAvater(this);
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        instance = this;
        KLog.init(BuildConfig.LOG_DEBUG);
        CrashReport.initCrashReport(this, "2d19fdd0a6", BuildConfig.LOG_DEBUG);
        setupApplicationComponent();
        setDatabase();
        Qsdk.init();
        RxTool.init(this);
        PRNGFixes.apply();
        ToastUtil.init();
        qlinkcom.init();
        initMoney();
        AppFilePath.init(this);
//        LeakCanary.install(this);
        NickUtil.initUserNickName(this);
//        mAppActivityManager = new AppActivityManager(this);
//        ProfileManager.getInstance(this).removeAllProfile(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotificationChannels();
        }
        initDbUpdate();
        initResumeListener();
        initMiPush();
        info = getPackageInfo(getPackageName());
        deviceStorage = Build.VERSION.SDK_INT < 24 ? this : new DeviceStorageApp(this);
        handler = new Handler(Looper.getMainLooper());
        mStatus = new StatusListener();
        mStatus.init(getApplicationContext());
        updateNotificationChannels();
//        remoteConfig = FirebaseRemoteConfig.getInstance();
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
                KLog.i(content, t);
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
//        configuration.locale = Locale.CHINA;
        configuration.locale = Locale.CHINA;
        //configuration.setLayoutDirection(Locale.CHINA);
        Locale.setDefault(Locale.CHINA);
//        if (SpUtil.getInt(this, ConstantValue.Language, 0) == 0) {
//        } else {
//            configuration.locale = Locale.CHINA;
//        }
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

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannels() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Background message
        CharSequence name = getString(R.string.channel_name_background);
        NotificationChannel mChannel = new NotificationChannel(OpenVPNService.NOTIFICATION_CHANNEL_BG_ID,
                name, NotificationManager.IMPORTANCE_MIN);

        mChannel.setDescription(getString(R.string.channel_description_background));
        mChannel.enableLights(false);

        mChannel.setLightColor(Color.DKGRAY);
        mNotificationManager.createNotificationChannel(mChannel);

        // Connection status change messages

        name = getString(R.string.channel_name_status);
        mChannel = new NotificationChannel(OpenVPNService.NOTIFICATION_CHANNEL_NEWSTATUS_ID,
                name, NotificationManager.IMPORTANCE_LOW);

        mChannel.setDescription(getString(R.string.channel_description_status));
        mChannel.enableLights(true);

        mChannel.setLightColor(Color.BLUE);
        mNotificationManager.createNotificationChannel(mChannel);
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

    public void startService() {
        Intent intent = new Intent(this, BaseService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    public void reloadService() {
        sendBroadcast(new Intent(Action.RELOAD));
    }

    public void stopService() {
        sendBroadcast(new Intent(Action.CLOSE));
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

    public Profile switchProfile(long id) {
        Profile result = null;
        try {
            result = ProfileManager.INSTANCE.getProfile(id) == null? ProfileManager.INSTANCE.createProfile(new Profile()): ProfileManager.INSTANCE.getProfile(id);
        } catch (Exception e) {

        }
        DataStore.INSTANCE.setProfileId(result.getId());
        return result;
    }

}
