package com.stratagile.qlink.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.stratagile.qlink.utils.SpUtil;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.qlink.ServiceConnectedEvent;
import com.stratagile.qlink.entity.wifi.ConnectedWifi;
import com.stratagile.qlink.qlink.Qsdk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huzhipeng on 2018/1/31.
 */

/**
 * wifi的分享者的服务，当有好友在使用该用户分享的wifi时，该服务就会启动，记录wifi连接的数量，管理wifi的心跳
 */
public class ServiceConnectedWIfiRecordSevice extends Service {

    private Map<String, ArrayList<ConnectedWifi>> mWifiMap;

    private CheckHeartBetThread checkHeartBetThread;

//    public class ServiceConnectedBinder extends Binder {
//        public ServiceConnectedWIfiRecordSevice getService(){
//            return ServiceConnectedWIfiRecordSevice.this;
//        }
//    }
//
//    private ServiceConnectedBinder binder = new ServiceConnectedBinder();、

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    public boolean onUnbind(Intent intent) {
//
//        return false;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWifiMap = new HashMap<>();
        List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll();
        for (WifiEntity wifiEntity : wifiEntityList) {
            if (wifiEntity.getOwnerP2PId().equals(SpUtil.getString(this, ConstantValue.P2PID, ""))) {
                KLog.i("mWifiMap添加" + wifiEntity.getSsid());
                mWifiMap.put(wifiEntity.getSsid(), new ArrayList<ConnectedWifi>());
            }
        }
        checkHeartBetThread = new CheckHeartBetThread();
        checkHeartBetThread.start();
        EventBus.getDefault().register(this);
        KLog.i("wifi提供者端的服务启动了。。。。。。。。。。");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlerEvent(ServiceConnectedEvent serviceConnectedEvent) {
        ArrayList<ConnectedWifi> useingFriendNumList = mWifiMap.get(serviceConnectedEvent.getSsid());
        //不是我注册的wifi，
        KLog.i("收到心跳包了");
        if (useingFriendNumList == null) {
            return;
        }
        //如果是wifi心跳
        if (serviceConnectedEvent.getUseType() == 0) {
            //如果是连接
            if (serviceConnectedEvent.getEventType() == 0 || serviceConnectedEvent.getEventType() == 1) {
                boolean isContain = false;
                if (useingFriendNumList.size() == 0) {
                    useingFriendNumList.add(new ConnectedWifi(serviceConnectedEvent.getFriendNum(), Calendar.getInstance().getTimeInMillis(), serviceConnectedEvent.getSsid()));
                }
                for (ConnectedWifi connectedWifi : useingFriendNumList) {
                    if (connectedWifi.getFriendNum() == serviceConnectedEvent.getFriendNum()) {
                        connectedWifi.setUpdateTime(Calendar.getInstance().getTimeInMillis());
                        isContain = true;
                        break;
                    }
                }
                if (!isContain) {
                    useingFriendNumList.add(new ConnectedWifi(serviceConnectedEvent.getFriendNum(), Calendar.getInstance().getTimeInMillis(), serviceConnectedEvent.getSsid()));
                }
                for (WifiEntity wifiEntity : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                    if (wifiEntity.getSsid().equals(serviceConnectedEvent.getSsid())) {
                        wifiEntity.setConnectCount(useingFriendNumList.size());
                        AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                        break;
                    }
                }
            }
            //如果是心跳
//            if (serviceConnectedEvent.getEventType() == 1) {
//                KLog.i("接受到了" + serviceConnectedEvent.getFriendNum() + "发过来的心跳");
//                //记录心跳时间，超时就是断开了。
//                ArrayList<ConnectedWifi> connectedWifiArrayList = mWifiMap.get(serviceConnectedEvent.getSsid());
//                for (ConnectedWifi connectedWifi : connectedWifiArrayList) {
//                    if (connectedWifi.getFriendNum() == serviceConnectedEvent.getFriendNum()) {
//                        connectedWifi.setUpdateTime(Calendar.getInstance().getTimeInMillis());
//                    }
//                }
//            }
            //如果是断开
            if (serviceConnectedEvent.getEventType() == 2) {
                useingFriendNumList.remove(serviceConnectedEvent.getFriendNum() + "");
            }
        }
        mWifiMap.put(serviceConnectedEvent.getSsid(), useingFriendNumList);
        //告诉该好友，自己的WiFi的连接数量
        List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll();
        for (WifiEntity wifiEntity : wifiEntityList) {
            if (wifiEntity.getSsid().equals(serviceConnectedEvent.getSsid())) {
                wifiEntity.setConnectCount(mWifiMap.get(serviceConnectedEvent.getSsid()).size());
                AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                Qsdk.getInstance().sendWifiBaseInfoRsp(wifiEntity, serviceConnectedEvent.getFriendNum());
                //刷新自己的wifi列表
                EventBus.getDefault().post(new ArrayList<WifiEntity>());
                break;
            }
        }
    }

    private class CheckHeartBetThread extends Thread {
        @Override
        public void run() {
            try {
                for (Map.Entry<String, ArrayList<ConnectedWifi>> entry : mWifiMap.entrySet()) {
                    for (ConnectedWifi connectedWifi : entry.getValue()) {
                        if (Calendar.getInstance().getTimeInMillis() - connectedWifi.getUpdateTime() > 1000 * 5) {
                            KLog.i("心跳超时，删除该使用者");
                            mWifiMap.get(entry.getKey()).remove(connectedWifi);
                            for (WifiEntity wifiEntity : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                                if (wifiEntity.getSsid().equals(entry.getKey())) {
                                    wifiEntity.setConnectCount(mWifiMap.get(entry.getKey()).size());
                                    AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                                    EventBus.getDefault().post(new ArrayList<WifiEntity>());
                                }
                            }
                            break;
                        }
                    }
                }
                Thread.sleep(1000 * 70);
                run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
