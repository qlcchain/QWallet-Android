package com.today.step.lib;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;

import com.google.gson.Gson;
import com.today.step.net.Auth;
import com.today.step.net.CHttpApiWrapper;
import com.today.step.net.ClaimQgas;
import com.today.step.net.CreateRecord;
import com.today.step.net.EpidemicList;
import com.today.step.net.HttpInfoInterceptor;
import com.today.step.net.RequestBodyInterceptor;
import com.today.step.net.StepSpUtil;
import com.today.step.net.VersionUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.today.step.lib.SportStepJsonUtils.getCalorieByStep;
import static com.today.step.lib.SportStepJsonUtils.getDistanceByStep;

public class TodayStepService extends Service implements Handler.Callback {

    private static final String TAG = "TodayStepService";

    private static final String STEP_CHANNEL_ID = "stepChannelId";

    /**
     * 步数通知ID
     */
    private static final int NOTIFY_ID = 1000;

    /**
     * 保存数据库频率
     */
    private static final int DB_SAVE_COUNTER = 300;

    /**
     * 传感器刷新频率
     */
    private static final int SAMPLING_PERIOD_US = SensorManager.SENSOR_DELAY_FASTEST;

    /**
     * 运动停止保存步数
     */
    private static final int HANDLER_WHAT_SAVE_STEP = 0;

    /**
     * 刷新通知栏步数
     */
    private static final int HANDLER_WHAT_REFRESH_NOTIFY_STEP = 2;


    /**
     * 如果走路如果停止，10秒钟后保存数据库
     */
    private static final int LAST_SAVE_STEP_DURATION = 10 * 1000;

    /**
     * 刷新通知栏步数，3s一次
     */
    private static final int REFRESH_NOTIFY_STEP_DURATION = 10 * 1000;

    /**
     * 点击通知栏广播requestCode
     */
    private static final int BROADCAST_REQUEST_CODE = 100;

    public static final String INTENT_NAME_0_SEPARATE = "intent_name_0_separate";
    public static final String INTENT_NAME_BOOT = "intent_name_boot";
    public static final String INTENT_STEP_INIT = "intent_step_init";

    /**
     * 当前步数
     */
    private static int CURRENT_STEP = 0;

    private SensorManager mSensorManager;
    /**
     * Sensor.TYPE_ACCELEROMETER
     * 加速度传感器计算当天步数，需要保持后台Service
     */
//    private TodayStepDetector mStepDetector;
    /**
     * Sensor.TYPE_STEP_COUNTER
     * 计步传感器计算当天步数，不需要后台Service
     */
    private TodayStepCounter mStepCounter;

    private NotificationManager nm;

    private boolean mSeparate = false;
    private boolean mBoot = false;

    /**
     * 保存数据库计数器
     */
    private int mDbSaveCount = 0;

    /**
     * 数据库
     */
    private ITodayStepDBHelper mTodayStepDBHelper;

    private final Handler sHandler = new Handler(this);

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLER_WHAT_SAVE_STEP: {
                //走路停止保存数据库
                mDbSaveCount = 0;

                saveDb(true, CURRENT_STEP);
                break;
            }
            case HANDLER_WHAT_REFRESH_NOTIFY_STEP: {
                //刷新通知栏

                updateTodayStep(CURRENT_STEP);

                sHandler.removeMessages(HANDLER_WHAT_REFRESH_NOTIFY_STEP);
                sHandler.sendEmptyMessageDelayed(HANDLER_WHAT_REFRESH_NOTIFY_STEP, REFRESH_NOTIFY_STEP_DURATION);
                break;
            }
            default:
                break;
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTodayStepDBHelper = TodayStepDBHelper.factory(getApplicationContext());

        mSensorManager = (SensorManager) this
                .getSystemService(SENSOR_SERVICE);

        initNotification(CURRENT_STEP);

//        getSensorRate();

//        JLogger.i(TAG, "onCreate currStep :" + CURRENT_STEP);
        Map<String, String> map = getLogMap();
        map.put("current_step", String.valueOf(CURRENT_STEP));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            mSeparate = intent.getBooleanExtra(INTENT_NAME_0_SEPARATE, false);
            mBoot = intent.getBooleanExtra(INTENT_NAME_BOOT, false);
            String setStep = intent.getStringExtra(INTENT_STEP_INIT);
//            if (!TextUtils.isEmpty(setStep)) {
//                try {
//                    setSteps(Integer.parseInt(setStep));
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//            }
        }
//        JLogger.i(TAG, "onStartCommand CurrStep :" + CURRENT_STEP + "   mSeparate:" + mSeparate + "    mBoot:" + mBoot);
        mDbSaveCount = 0;

        Map<String, String> map = getLogMap();
        map.put("current_step", String.valueOf(CURRENT_STEP));
        map.put("mSeparate", String.valueOf(mSeparate));
        map.put("mBoot", String.valueOf(mBoot));
        map.put("mDbSaveCount", String.valueOf(mDbSaveCount));

        updateNotification(CURRENT_STEP);
        //注册传感器
        startStepDetector();

        sHandler.removeMessages(HANDLER_WHAT_REFRESH_NOTIFY_STEP);
        sHandler.sendEmptyMessageDelayed(HANDLER_WHAT_REFRESH_NOTIFY_STEP, REFRESH_NOTIFY_STEP_DURATION);

        return START_STICKY;
    }

    private synchronized void initNotification(int currentStep) {

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int smallIcon = getResources().getIdentifier("icon_step_small", "mipmap", getPackageName());
        if (0 == smallIcon) {
            smallIcon = R.mipmap.ic_launcher;
        }
        String receiverName = getReceiver(getApplicationContext());
        PendingIntent contentIntent = PendingIntent.getBroadcast(this, BROADCAST_REQUEST_CODE, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        if (!TextUtils.isEmpty(receiverName)) {
            try {
                contentIntent = PendingIntent.getBroadcast(this, BROADCAST_REQUEST_CODE, new Intent(this, Class.forName(receiverName)), PendingIntent.FLAG_UPDATE_CURRENT);
            } catch (Exception e) {
                e.printStackTrace();
                contentIntent = PendingIntent.getBroadcast(this, BROADCAST_REQUEST_CODE, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            }
        }
        String km = getDistanceByStep(currentStep);
        String calorie = getCalorieByStep(currentStep);
        String contentText = calorie + " 千卡  " + km + " 公里";
        int largeIcon = getResources().getIdentifier("ic_launcher", "mipmap", getPackageName());
        Bitmap largeIconBitmap = null;
        if (0 != largeIcon) {
            largeIconBitmap = BitmapFactory.decodeResource(getResources(), largeIcon);
        } else {
            largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
//        mNotificationApiCompat = new NotificationApiCompat.Builder(this,
//                nm,
//                STEP_CHANNEL_ID,
//                getString(R.string.step_channel_name),
//                smallIcon)
//                .setContentIntent(contentIntent)
//                .setContentText(contentText)
//                .setContentTitle(getString(R.string.title_notification_bar, String.valueOf(currentStep)))
//                .setTicker(getString(R.string.app_name))
//                .setOngoing(true)
//                .setPriority(Notification.PRIORITY_MIN)
//                .setLargeIcon(largeIconBitmap)
//                .setOnlyAlertOnce(true)
//                .builder();
//        mNotificationApiCompat.startForeground(this, NOTIFY_ID);
//        mNotificationApiCompat.notify(NOTIFY_ID);

    }

    @Override
    public IBinder onBind(Intent intent) {
//        JLogger.i(TAG, "onBind : CurrStep : " + CURRENT_STEP);
        initNet();
        Map<String, String> map = getLogMap();
        map.put("current_step", String.valueOf(CURRENT_STEP));
//        JLoggerWraper.onEventInfo(this, JLoggerConstant.JLOGGER_SERVICE_ONBIND, map);


        sHandler.removeMessages(HANDLER_WHAT_REFRESH_NOTIFY_STEP);
        sHandler.sendEmptyMessageDelayed(HANDLER_WHAT_REFRESH_NOTIFY_STEP, REFRESH_NOTIFY_STEP_DURATION);

        return mIBinder.asBinder();
    }

    private void startStepDetector() {

        //android4.4以后如果有stepcounter可以使用计步传感器
        if (getStepCounter()) {
            addStepCounterListener();
        } else {
            //addBasePedoListener();
        }
    }

    private void addStepCounterListener() {
        if (null != mStepCounter) {
            WakeLockUtils.getLock(this);
            CURRENT_STEP = mStepCounter.getCurrentStep();
            updateNotification(CURRENT_STEP);
            Map<String, String> map = getLogMap();
            map.put("current_step", String.valueOf(CURRENT_STEP));
//            JLoggerWraper.onEventInfo(this, JLoggerConstant.JLOGGER_SERVICE_TYPE_STEP_COUNTER_HADREGISTER, map);
            return;
        }
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (null == countSensor) {
            return;
        }
        mStepCounter = new TodayStepCounter(getApplicationContext(), mOnStepCounterListener, mSeparate, mBoot);
        CURRENT_STEP = mStepCounter.getCurrentStep();
        Log.i("TodayStepService", "1当前步数为：" + CURRENT_STEP);
        boolean registerSuccess = mSensorManager.registerListener(mStepCounter, countSensor, SAMPLING_PERIOD_US);
        Map<String, String> map = getLogMap();
        map.put("current_step", String.valueOf(CURRENT_STEP));
        map.put("current_step_registerSuccess", String.valueOf(registerSuccess));
//        JLoggerWraper.onEventInfo(this, JLoggerConstant.JLOGGER_SERVICE_TYPE_STEP_COUNTER_REGISTER, map);
    }

//    private void addBasePedoListener() {
//
//        if (null != mStepDetector) {
//            WakeLockUtils.getLock(this);
//            CURRENT_STEP = mStepDetector.getCurrentStep();
////            JLogger.i(TAG, "alreadly register TYPE_ACCELEROMETER : CurrStep : " + CURRENT_STEP);
//            updateNotification(CURRENT_STEP);
//            Map<String, String> map = getLogMap();
//            map.put("current_step", String.valueOf(CURRENT_STEP));
////            JLoggerWraper.onEventInfo(this, JLoggerConstant.JLOGGER_SERVICE_TYPE_ACCELEROMETER_HADREGISTER, map);
//
//            return;
//        }
//        //没有计步器的时候开启定时器保存数据
//        Sensor sensor = mSensorManager
//                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        if (null == sensor) {
//            return;
//        }
//        mStepDetector = new TodayStepDetector(this, mOnStepCounterListener);
//        CURRENT_STEP = mStepDetector.getCurrentStep();
//        // 获得传感器的类型，这里获得的类型是加速度传感器
//        // 此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
//        boolean registerSuccess = mSensorManager.registerListener(mStepDetector, sensor, SAMPLING_PERIOD_US);
//        Map<String, String> map = getLogMap();
//        map.put("current_step", String.valueOf(CURRENT_STEP));
//        map.put("current_step_registerSuccess", String.valueOf(registerSuccess));
////        JLoggerWraper.onEventInfo(this, JLoggerConstant.JLOGGER_SERVICE_TYPE_ACCELEROMETER_REGISTER, map);
//    }

    @Override
    public void onDestroy() {
//        JLoggerWraper.onEventInfo(this, JLoggerConstant.JLOGGER_TODAYSTEPSERVICE_ONDESTROY, "CURRENT_STEP=" + CURRENT_STEP);
//        JLoggerWraper.flush();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        JLoggerWraper.onEventInfo(this, JLoggerConstant.JLOGGER_TODAYSTEPSERVICE_ONUNBIND, "CURRENT_STEP=" + CURRENT_STEP);
        return super.onUnbind(intent);
    }

    /**
     * 步数每次回调的方法
     *
     * @param currentStep
     */
    private void updateTodayStep(int currentStep) {

        CURRENT_STEP = currentStep;
        updateNotification(CURRENT_STEP);
        saveStep(currentStep);
    }

    private void saveStep(int currentStep) {
        Log.i("TodayStepService", "saveStep 1");
        sHandler.removeMessages(HANDLER_WHAT_SAVE_STEP);
        sHandler.sendEmptyMessageDelayed(HANDLER_WHAT_SAVE_STEP, LAST_SAVE_STEP_DURATION);

//        if (DB_SAVE_COUNTER > mDbSaveCount) {
//            mDbSaveCount++;
//
//            return;
//        }
//        mDbSaveCount = 0;
//
        saveDb(false, currentStep);
    }

    /**
     * @param handler     true handler回调保存步数，否false
     * @param currentStep
     */
    private void saveDb(boolean handler, int currentStep) {
        Log.i("TodayStepService", "保存步数1");
        TodayStepData todayStepData = new TodayStepData();
        todayStepData.setToday(getTodayDate());
        todayStepData.setDate(System.currentTimeMillis());
        todayStepData.setStep(currentStep);
        if (null != mTodayStepDBHelper) {
            if (!mTodayStepDBHelper.isExist(todayStepData)) {
                Log.i("TodayStepService", "保存步数2");
                mTodayStepDBHelper.clear(getTodayDate());
                mTodayStepDBHelper.insert(todayStepData);
            } else {
                mTodayStepDBHelper.insert(todayStepData);
            }
        }
    }

    private void cleanDb() {

//        JLogger.i(TAG, "cleanDb");
        Map<String, String> map = getLogMap();
        map.put("cleanDB_current_step", String.valueOf(CURRENT_STEP));
//        JLoggerWraper.onEventInfo(this, JLoggerConstant.JLOGGER_SERVICE_CLEAN_DB, map);
        mDbSaveCount = 0;

        if (null != mTodayStepDBHelper) {
            //保存多天的步数
            mTodayStepDBHelper.deleteTable();
            mTodayStepDBHelper.createTable();
        }
    }

    private String getTodayDate() {
        return DateUtils.getCurrentDate("yyyy-MM-dd");
    }

    /**
     * 更新通知
     */
    private synchronized void updateNotification(int stepCount) {
//        if (null != mNotificationApiCompat) {
//            String km = getDistanceByStep(stepCount);
//            String calorie = getCalorieByStep(stepCount);
//            String contentText = calorie + " 千卡  " + km + " 公里";
//            mNotificationApiCompat.updateNotification(NOTIFY_ID, getString(R.string.title_notification_bar, String.valueOf(stepCount)), contentText);
//        }
    }

    private boolean getStepCounter() {
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (null == countSensor) {
            return false;
        }
        return true;
    }

    private OnStepCounterListener mOnStepCounterListener = new OnStepCounterListener() {
        @Override
        public void onChangeStepCounter(int step) {
            Log.i("TodayStepService", "步数刷新了");
            CURRENT_STEP = step;
            updateTodayStep(CURRENT_STEP);
        }

        @Override
        public void onStepCounterClean() {

            CURRENT_STEP = 0;
            updateNotification(CURRENT_STEP);

//            cleanDb();
        }

    };

    private final ISportStepInterface.Stub mIBinder = new ISportStepInterface.Stub() {

        private JSONArray getSportStepJsonArray(List<TodayStepData> todayStepDataArrayList) {
            return SportStepJsonUtils.getSportStepJsonArray(todayStepDataArrayList);
        }

        @Override
        public int getCurrentTimeSportStep() throws RemoteException {
            return CURRENT_STEP;
        }

        @Override
        public String getTodaySportStepArray() throws RemoteException {
            if (null != mTodayStepDBHelper) {
                List<TodayStepData> todayStepDataArrayList = mTodayStepDBHelper.getQueryAll();
                JSONArray jsonArray = getSportStepJsonArray(todayStepDataArrayList);
                return new Gson().toJson(todayStepDataArrayList);
            }
            return null;
        }

        @Override
        public String createRecord(String account, String token) throws RemoteException {

            Log.i("TodayStepService", "首次进入请求");
            HashMap<String, String> infoMap = new HashMap<>();
            infoMap.put("account", account);
            infoMap.put("token", token);
            infoMap.put("stopNumber", CURRENT_STEP + "");
            Log.i("TodayStepService", "传的步数为：" + CURRENT_STEP);
            CHttpApiWrapper.getInstance(retrofit).createRecord(infoMap).subscribe(new Consumer<CreateRecord>() {
                @Override
                public void accept(CreateRecord baseBack) throws Exception {
                    //isSuccesse
                    Log.i("TodayStepService", "网络返回");
                    Log.i("TodayStepService", baseBack.toString());
                    baseBack.setCreate(true);
                    EventBus.getDefault().post(baseBack);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    EventBus.getDefault().post(throwable);
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    //onComplete
                }
            });
            return null;
        }

        @Override
        public void focusEpidemic(String account, String token) throws RemoteException {
            HashMap<String, String> infoMap = new HashMap<>();
            infoMap.put("account", account);
            infoMap.put("token", token);
            infoMap.put("stopNumber", CURRENT_STEP + "");
            Log.i("TodayStepService", "传的步数为：" + CURRENT_STEP);
            CHttpApiWrapper.getInstance(retrofit).focusEpidemic(infoMap).subscribe(new Consumer<CreateRecord>() {
                @Override
                public void accept(CreateRecord baseBack) throws Exception {
                    //isSuccesse
                    Log.i("TodayStepService", "网络返回");
                    Log.i("TodayStepService", baseBack.toString());
                    baseBack.setCreate(false);
                    EventBus.getDefault().post(baseBack);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    EventBus.getDefault().post(throwable);
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    //onComplete
                }
            });
        }

        @Override
        public void getEpidemicList(String account, String token, final String status) throws RemoteException {
            HashMap<String, String> infoMap = new HashMap<>();
            infoMap.put("account", account);
            infoMap.put("token", token);
            infoMap.put("status", status);
            CHttpApiWrapper.getInstance(retrofit).eepidemicList(infoMap).subscribe(new Consumer<EpidemicList>() {
                @Override
                public void accept(EpidemicList baseBack) throws Exception {
                    //isSuccesse
                    Log.i("TodayStepService", "网络返回");
                    Log.i("TodayStepService", baseBack.toString());
                    baseBack.setStatus(status);
                    EventBus.getDefault().post(baseBack);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    EventBus.getDefault().post(throwable);
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    //onComplete
                }
            });
        }

        @Override
        public void claimQgas(String account, String token, String recordId, String toAddress, String sid, String sig, String afsToken, String afsAppId, String afsScen) throws RemoteException {
            HashMap<String, String> infoMap = new HashMap<>();
            infoMap.put("account", account);
            infoMap.put("token", token);
            infoMap.put("recordId", recordId);
            infoMap.put("toAddress", toAddress);
            infoMap.put("sessionId", sid);
            infoMap.put("sig", sig);
            infoMap.put("afsToken", afsToken);
            infoMap.put("appKey", afsAppId);
            infoMap.put("scene", afsScen);
            CHttpApiWrapper.getInstance(retrofit).epidemicReceive(infoMap).subscribe(new Consumer<ClaimQgas>() {
                @Override
                public void accept(ClaimQgas baseBack) throws Exception {
                    //isSuccesse
                    Log.i("TodayStepService", "网络返回");
                    Log.i("TodayStepService", baseBack.toString());
                    EventBus.getDefault().post(baseBack);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    EventBus.getDefault().post(throwable);
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    //onComplete
                }
            });
        }

        @Override
        public void claimQlc(String account, String token, String toAddress, String sid, String sig, String afsToken, String afsAppId, String afsScen) throws RemoteException {
            HashMap<String, String> infoMap = new HashMap<>();
            infoMap.put("account", account);
            infoMap.put("token", token);
            infoMap.put("toAddress", toAddress);
            infoMap.put("sessionId", sid);
            infoMap.put("sig", sig);
            infoMap.put("afsToken", afsToken);
            infoMap.put("appKey", afsAppId);
            infoMap.put("scene", afsScen);
            CHttpApiWrapper.getInstance(retrofit).epidemicReceiveQlc(infoMap).subscribe(new Consumer<ClaimQgas>() {
                @Override
                public void accept(ClaimQgas baseBack) throws Exception {
                    //isSuccesse
                    Log.i("TodayStepService", "网络返回");
                    Log.i("TodayStepService", baseBack.toString());
                    EventBus.getDefault().post(baseBack);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    EventBus.getDefault().post(throwable);
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    //onComplete
                }
            });
        }
    };

    public static String getReceiver(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_RECEIVERS);
            ActivityInfo[] activityInfos = packageInfo.receivers;
            if (null != activityInfos && activityInfos.length > 0) {
                for (int i = 0; i < activityInfos.length; i++) {
                    String receiverName = activityInfos[i].name;
                    Class superClazz = Class.forName(receiverName).getSuperclass();
                    int count = 1;
                    while (null != superClazz) {
                        if (superClazz.getName().equals("java.lang.Object")) {
                            break;
                        }
                        if (superClazz.getName().equals(BaseClickBroadcast.class.getName())) {
                            return receiverName;
                        }
                        if (count > 20) {
                            //用来做容错，如果20个基类还不到Object直接跳出防止while死循环
                            break;
                        }
                        count++;
                        superClazz = superClazz.getSuperclass();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取传感器速率
     */
//    private void getSensorRate() {
//
//        Class<?> personType = SensorManager.class;
//
//        //访问私有方法
//        //getDeclaredMethod可以获取到所有方法，而getMethod只能获取public
//        Method method = null;
//        try {
//            method = personType.getDeclaredMethod("getDelay", int.class);
//            //压制Java对访问修饰符的检查
//            method.setAccessible(true);
//            //调用方法;person为所在对象
//            int rate = (int) method.invoke(null, SAMPLING_PERIOD_US);
//            Map<String, String> map = getLogMap();
//            map.put("getSensorRate", String.valueOf(rate));
////            JLoggerWraper.onEventInfo(this, JLoggerConstant.JLOGGER_SERVICE_SENSORRATE_INVOKE, map);
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 设置步数初始值，目前只支持设置用加速度传感器进行计步
     *
     * @param steps
     */
//    private void setSteps(int steps) {
//        if (null != mStepDetector) {
//            mStepDetector.setCurrentStep(steps);
//        }
//    }

    private Map<String, String> map;

    private Map<String, String> getLogMap() {
        if (map == null) {
            map = new HashMap<>();
        } else {
            map.clear();
        }
        return map;
    }

    private Retrofit retrofit;

    private void initNet() {
        final OkHttpClient.Builder builderOkHttpClient = new OkHttpClient.Builder();
        builderOkHttpClient.connectTimeout(6000, TimeUnit.MILLISECONDS)
                .readTimeout(6000, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpInfoInterceptor());
        builderOkHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Auth auth = new Auth();
                String uuid = "";
                auth.setAgent(WebSettings.getDefaultUserAgent(TodayStepService.this));
                try {
//                    String serieaId = RxDeviceTool.getSerialNumberNew();
//                    auth.setUuid(serieaId);
                } catch (Exception e) {
                    e.printStackTrace();
                    auth.setUuid("");
                }
                auth.setPlatform("Android");
                auth.setAppVersion(VersionUtil.getAppVersionName(TodayStepService.this));
                auth.setAppBuild(VersionUtil.getAppVersionCode(TodayStepService.this) + "");
                Request request = chain.request()
                        .newBuilder()
                        .removeHeader("User-Agent")//移除旧的
                        .addHeader("User-Agent", new Gson().toJson(auth))//添加真正的头部
                        .build();
                return chain.proceed(request);
            }
        });


        Retrofit.Builder builder = new Retrofit.Builder();
        if (StepSpUtil.getBoolean(TodayStepService.this, "stepMainNet", true)) {
            //主网
            builder.client(builderOkHttpClient.build())
                    .baseUrl("http://dapp-t.qlink.mobi/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            retrofit = builder.build();
        } else {
            //测试网
            builder.client(builderOkHttpClient.build())
                    .baseUrl("https://www.qlc.mobi/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            retrofit = builder.build();
        }
    }


}
