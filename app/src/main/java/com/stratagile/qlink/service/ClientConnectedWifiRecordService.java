package com.stratagile.qlink.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.BroadCastAction;
import com.stratagile.qlink.db.WifiEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huzhipeng on 2018/1/20.
 * <p>
 * 客户端的服务，wifi使用者，需要记录wifi的使用时间，使用量，管理心跳
 * <p>
 * <p>
 * 使用Service的步骤：
 * <p>
 * 1.定义一个类继承Service
 * 2.在Manifest.xml文件中配置该Service
 * 3.使用Context的startService(Intent)方法启动该Service
 * 4.不再使用时，调用stopService(Intent)方法停止该服务
 * <p>
 * 使用这种start方式启动的Service的生命周期如下：
 * onCreate()--->onStartCommand()（onStart()方法已过时） ---> onDestory()
 * <p>
 * 说明：如果服务已经开启，不会重复的执行onCreate()， 而是会调用onStart()和onStartCommand()。
 * 服务停止的时候调用 onDestory()。服务只会被停止一次。
 * <p>
 * 特点：一旦服务开启跟调用者(开启者)就没有任何关系了。
 * 开启者退出了，开启者挂了，服务还在后台长期的运行。
 * 开启者不能调用服务里面的方法。
 */

public class ClientConnectedWifiRecordService extends Service {

    private WifiManager wifiManager;
    private ConnectivityManager cManager;
    long mobileRx;
    long mobileTx;
    long mWifiTotal;
//    private HeartBetThread heartBetThread;
//    MonitorWifiThread monitorWifiThread;
    private WifiEntity useingWifiEntity;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
                    KLog.i("WIFI_STATE_DISABLING");
                }
            } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                KLog.i("CONNECTIVITY_ACTION");
                NetworkInfo networkInfo = cManager.getActiveNetworkInfo();
                if (networkInfo == null) {
                    return;
                }
                NetworkInfo.State state = networkInfo.getState();
                KLog.i(state);
                if (state == NetworkInfo.State.CONNECTED) {
                    KLog.i("State.CONNECTED");
                    switch (networkInfo.getType()) {
                        case ConnectivityManager.TYPE_WIFI:
                            KLog.i("变为WiFi网络了");
//                            monitorWifiThread.onThreadResume();
                            for (WifiEntity wifiEntity : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                                if (wifiEntity.getIsConnected()) {
                                    wifiEntity.setIsConnected(false);
                                    AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                                }
                            }

                            for (WifiEntity wifiEntity : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                                // && wifiEntity.getMacAdrees().equals(wifiManager.getConnectionInfo().getBSSID())
                                if (wifiEntity.getSsid() != null && wifiEntity.getSsid().equals(wifiManager.getConnectionInfo().getSSID().replace("\"", ""))&& wifiEntity.getMacAdrees() != null  && wifiEntity.getMacAdrees().equals(wifiManager.getConnectionInfo().getBSSID())) {
                                    wifiEntity.setIsConnected(true);
                                    useingWifiEntity = wifiEntity;
                                    AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                                    KLog.i("更新wifi列表的状态");
                                    EventBus.getDefault().post(new ArrayList<WifiEntity>());
                                    break;
                                }
                            }
                            KLog.i("wifi的ssid为:" + wifiManager.getConnectionInfo().getSSID());
                            KLog.i("wifi的mac地址为:" + wifiManager.getConnectionInfo().getBSSID());
                            break;
                        case ConnectivityManager.TYPE_MOBILE:
                            KLog.i("变为手机网络了");
//                            monitorWifiThread.onThreadWait();
                            for (WifiEntity wifiEntity : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                                if (wifiEntity.getIsConnected()) {
                                    wifiEntity.setIsConnected(false);
                                    AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                                }
                            }
                            EventBus.getDefault().post(new ArrayList<WifiEntity>());
                            break;
                        default:
                            break;
                    }
                    // 开始不断获取最近的流量信息，值为0时，跳过

                } else if (state == NetworkInfo.State.DISCONNECTED) {
//                    monitorWifiThread.onThreadWait();
                    for (WifiEntity wifiEntity : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                        if (wifiEntity.getIsConnected()) {
                            wifiEntity.setIsConnected(false);
                            AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                        }
                    }
                    EventBus.getDefault().post(new ArrayList<WifiEntity>());
                }
            } else if (action.equals(BroadCastAction.disconnectWiFi)) {
                wifiManager.removeNetwork(wifiManager.getConnectionInfo().getNetworkId());
                wifiManager.saveConfiguration();
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.i("wifi使用者端的服务启动了。。。。。。。。。。");
        cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll();
        for (WifiEntity wifiEntity : wifiEntityList) {
            if (wifiEntity.getSsid()!= null && wifiEntity.getSsid().equals(wifiManager.getConnectionInfo().getSSID().replace("\"", "")) && wifiEntity.getMacAdrees()!= null && wifiEntity.getMacAdrees().equals(wifiManager.getConnectionInfo().getBSSID())) {
                wifiEntity.setIsConnected(true);
                useingWifiEntity = wifiEntity;
                AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                EventBus.getDefault().post(new ArrayList<WifiEntity>());
                break;
            }
        }
        long wifiDown = TrafficStats.getTotalRxBytes() - TrafficStats.getMobileRxBytes();
        long wifiUp = TrafficStats.getTotalTxBytes() - TrafficStats.getMobileTxBytes();
        KLog.i("wifi下载总流量" + Formatter.formatFileSize(this, wifiDown));
        KLog.i("wifi上传总流量" + Formatter.formatFileSize(this, wifiUp));
        KLog.i("wifi总流量" + Formatter.formatFileSize(this, wifiDown + wifiUp));
        mWifiTotal = wifiDown + wifiUp;
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); // 添加接收网络连接状态改变的Action
        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mFilter.addAction(BroadCastAction.disconnectWiFi);
        registerReceiver(mReceiver, mFilter);
//        monitorWifiThread = new MonitorWifiThread();
//        monitorWifiThread.start();
//        heartBetThread = new HeartBetThread();
//        heartBetThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}
