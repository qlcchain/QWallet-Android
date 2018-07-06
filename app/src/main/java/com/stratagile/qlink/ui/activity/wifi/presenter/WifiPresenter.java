package com.stratagile.qlink.ui.activity.wifi.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.stratagile.qlink.R;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.utils.SpUtil;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.WifiRegisteResult;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.wifi.contract.WifiContract;
import com.stratagile.qlink.ui.activity.wifi.WifiFragment;
import com.stratagile.qlink.utils.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: presenter of WifiFragment
 * @date 2018/01/09 13:46:43
 */
public class WifiPresenter implements WifiContract.WifiContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final WifiContract.View mView;
    //    private CompositeDisposable mCompositeDisposable;
    private WifiFragment mFragment;
    private boolean isFirstInApp = true;

    private ArrayList<ScanResult> scanedWifiSsid = new ArrayList<>();
    /**
     * 临时扫描出来增加的WiFi
     */
    private ArrayList<ScanResult> tempAddSsid = new ArrayList<>();
    /**
     * 扫描完，减少的WiFi
     */
    private ArrayList<ScanResult> temDecreaseSsid = new ArrayList<>();
    /**
     * 临时扫描出来的WiFi
     */
    private ArrayList<ScanResult> currentWifi = new ArrayList<>();
    private boolean isGetWifiComplete = true;
    private ArrayList<WifiEntity> qlinkScanResultList = new ArrayList<>();

    @Inject
    public WifiPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull WifiContract.View view, WifiFragment fragment) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
//        mCompositeDisposable = new CompositeDisposable();
        this.mFragment = fragment;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
//        if (!mCompositeDisposable.isDisposed()) {
//            mCompositeDisposable.dispose();
//        }
    }

    @Override
    public void getRegistedSsid() {
        Map<String, Object> ssidMap = new HashMap<>();
        String[] ssids = new String[scanedWifiSsid.size()];
        for (int i = 0; i < scanedWifiSsid.size(); i++) {
            ssids[i] = scanedWifiSsid.get(i).SSID;
        }
        ssidMap.put("ssIds", ssids);
        getWifiInfoFromService(ssidMap);
        if (isFirstInApp) {
            handleServiceResult(null);
            isFirstInApp = false;
        }
    }

    private void getWifiInfoFromService(Map map) {
        qlinkScanResultList.clear();
        httpAPIWrapper.getRegistedSsid(map).subscribe(new Observer<WifiRegisteResult>() {
            Disposable disposable;
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(WifiRegisteResult wifiRegisteResult) {
                KLog.i("onSuccesse");
                handleServiceResult(wifiRegisteResult.getData());
                disposable.dispose();
            }

            @Override
            public void onError(Throwable e) {
                KLog.i("onError");
                isGetWifiComplete = true;
                handleServiceResult(null);
                e.printStackTrace();
                disposable.dispose();
            }

            @Override
            public void onComplete() {
                isGetWifiComplete = true;
                handleServiceResult(null);
                KLog.i("onComplete");
                disposable.dispose();
            }
        });
//        Disposable disposable = httpAPIWrapper.getRegistedSsid(map)
//                .subscribe(new Consumer<WifiRegisteResult>() {
//                    @Override
//                    public void accept(WifiRegisteResult result) throws Exception {
//                        //isSuccesse
//                        KLog.i("onSuccesse");
//                        handleServiceResult(result.getData());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        //onError
//                        KLog.i("onError");
//                        isGetWifiComplete = true;
//                        handleServiceResult(null);
//                        throwable.printStackTrace();
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        //onComplete
//                        isGetWifiComplete = true;
//                        handleServiceResult(null);
//                        KLog.i("onComplete");
//                    }
//                });
//        mCompositeDisposable.add(disposable);
    }

    /**
     * @see com.stratagile.qlink.ui.activity.wifi.WifiListFragment#setListData (EventBus.poss)
     * @param list
     */
    private void handleServiceResult(ArrayList<WifiRegisteResult.DataBean> list) {
        qlinkScanResultList.clear();
        List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().list();
        if (list == null) {
            for (ScanResult scanResult : scanedWifiSsid) {
                for (WifiEntity wifiEntity : wifiEntityList) {
                    if (scanResult.SSID.replace("\"", "").equals(wifiEntity.getSsid())) {
                        qlinkScanResultList.add(wifiEntity);
                    }
                }

            }
            KLog.i("刚刚打开app，为了显示数据，现将wifi列表展示出来");
            EventBus.getDefault().post(qlinkScanResultList);
            isGetWifiComplete = true;
            return;
        }
        Iterator it = wifiEntityList.iterator();        //根据传入的集合(旧集合)获取迭代器
        while (it.hasNext()) {          //遍历老集合
            WifiEntity obj = (WifiEntity) it.next();       //记录每一个元素
            boolean isHad = false;
            Iterator dataIt = list.iterator();        //根据传入的集合(旧集合)获取迭代器

            int j = list.size();
            for (int i = 0; i < j; i++) {
                WifiRegisteResult.DataBean objData = list.get(i);
                if(objData.getSsId().equals(obj.getSsid()))
                {
                    isHad = true;
                    break;
                }
            }
            if(!isHad && !obj.getOwnerP2PId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "")))
            {
                AppConfig.getInstance().getDaoSession().getWifiEntityDao().delete(obj);
            }
        }
        wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().list();
        int deleteFriendAllResult = -2;
        long lastDeleteFreindTime = SpUtil.getLong(AppConfig.getInstance(), ConstantValue.lastDeleteFreindTime, 0);
        long currentTime = new Date().getTime();
//        if (qlinkcom.GetP2PConnectionStatus() > 0 && (currentTime - lastDeleteFreindTime) > 24 * 60 * 60 * 1000) {
//            LogUtil.addLog("开始删除好友", getClass().getSimpleName());
//            deleteFriendAllResult = qlinkcom.DeleteFriendAll();
//            if(deleteFriendAllResult == 0)
//            {
//                //ConstantValue.isDeleteFreinds = true;
//                LogUtil.addLog("删除所有好友并且成功", getClass().getSimpleName());
//                SpUtil.putLong(AppConfig.getInstance(), ConstantValue.lastDeleteFreindTime, new Date().getTime());
//            }
//        }
        for (WifiRegisteResult.DataBean dataBean : list) {
            if (dataBean.getSsId().equals("")) {
                continue;
            }
//            KLog.i(dataBean.getSsId());
            for (WifiEntity wifiEntity : wifiEntityList) {
                if (dataBean.getSsId().equals(wifiEntity.getSsid())) {
                    //已经注册，更新信息，
                    if (dataBean.getP2pId() != null && !dataBean.getP2pId().equals("")) {
                        wifiEntity.setOwnerP2PId(dataBean.getP2pId());
                        wifiEntity.setRegiste(true);
                        wifiEntity.setAssetTranfer(dataBean.getQlc());
                        wifiEntity.setWalletAddress(dataBean.getAddress());
                        wifiEntity.setMacAdrees(dataBean.getMac());
                        wifiEntity.setRegisterQlc(dataBean.getRegisterQlc());
                        wifiEntity.setAssetTranfer(dataBean.getQlc());
                        wifiEntity.setPriceInQlc((float) dataBean.getCost());
                        wifiEntity.setDeviceAllowed(dataBean.getConnectNum());
                        wifiEntity.setAvatar(dataBean.getImgUrl());
//                        wifiEntity.setFreindNum(-1);
                        //判断是否是好友，是好友就把friendNum添加到WiFientity中，不是好友就要添加好友，再添加。
                        int friendNum = qlinkcom.GetFriendNumInFriendlist(dataBean.getP2pId());
                        byte[] p2pId = new byte[100];
                        String friendNumStr = "";
                        if (qlinkcom.GetFriendP2PPublicKey(wifiEntity.getOwnerP2PId(), p2pId) == 0) {
                            friendNumStr = new String(p2pId).trim();
                            KLog.i(friendNumStr);
                        }
                        if (friendNumStr == null) {
                            friendNumStr = "";
                        }
                        if (friendNum < 0 && !wifiEntity.getOwnerP2PId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
                            //不是好友，添加
                            KLog.i(friendNum + "需要添加好友" + wifiEntity.getSsid());
                            friendNum = qlinkcom.AddFriend(dataBean.getP2pId());
                            if (friendNum >= 0) {
                                wifiEntity.setFreindNum(friendNumStr);
                            }
                        } else {
//                            KLog.i(friendNum + "已经是好友" + wifiEntity.getSsid());
                            wifiEntity.setFreindNum(friendNumStr);
                        }
                        if (!wifiEntity.getFreindNum().equals("")) {
                            if (qlinkcom.GetFriendConnectionStatus(friendNumStr) > 0) {
                                wifiEntity.setOnline(true);
                            } else {
                                wifiEntity.setOnline(false);
                            }
                        }
                        AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                    } else {
                        //本来已经注册，被系统删除了，需要清除信息, 也不要加好友
                        if (dataBean.getP2pId().equals("")) {
                            wifiEntity.setOwnerP2PId("");
                            wifiEntity.setRegiste(false);
                            wifiEntity.setOnline(false);
                            wifiEntity.setWalletAddress("");
                            wifiEntity.setFreindNum("");
                            AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                        } else {
                            //没有注册,就不管了
                        }
                    }
                    break;
                }

            }
        }

        wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().list();
        for (ScanResult scanResult : scanedWifiSsid) {
            for (WifiEntity wifiEntity : wifiEntityList) {
                if (scanResult.SSID.replace("\"", "").equals(wifiEntity.getSsid())) {
                    qlinkScanResultList.add(wifiEntity);
                }
            }
        }
        EventBus.getDefault().post(qlinkScanResultList);
        boolean hasRegistedWifi = false;
        for (WifiEntity wifiEntity : qlinkScanResultList) {
            if (wifiEntity.isRegiste()) {
                hasRegistedWifi = true;
            }
        }
        if (!hasRegistedWifi) {
            mView.setPager(1);
        }
        isGetWifiComplete = true;
    }

    @Override
    public void requestPermission() {
        AndPermission.with(((Fragment) mView).getActivity())
                .requestCode(101)
                .permission(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET
                )
                .rationale((requestCode, rationale) -> {
                            AndPermission
                                    .rationaleDialog((((Fragment) mView).getActivity()), rationale)
                                    .setNegativeButton(AppConfig.getInstance().getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ToastUtil.show(((Activity) mView), AppConfig.getInstance().getResources().getString(R.string.permission_denied));
                                            mView.startUpdateWIfiInfo();
                                        }
                                    })
                                    .show();
                        }
                )
                .callback(permission)
                .start();
    }

    @Override
    public void handlerWifiChange(List<ScanResult> wifiList) {
        ArrayList<WifiEntity> showListwifi = new ArrayList<>();
//        if (!isGetWifiComplete) {
//            KLog.i("正在获取中，重复请求过滤掉");
//            return;
//        }
        isGetWifiComplete = false;
        scanedWifiSsid.clear();
        List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().list();
        ArrayList<String> newList = new ArrayList();     //创建新集合
        Iterator it = wifiEntityList.iterator();        //根据传入的集合(旧集合)获取迭代器
        while (it.hasNext()) {          //遍历老集合
            WifiEntity obj = (WifiEntity) it.next();       //记录每一个元素
            if (!newList.contains(obj.getSsid())) {      //如果新集合中不包含旧集合中的元素
                newList.add(obj.getSsid());       //将元素添加
            }else{
                AppConfig.getInstance().getDaoSession().getWifiEntityDao().delete(obj);
            }
        }
        wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().list();
        KLog.i("数据库中的WiFi数量为:" + wifiEntityList.size());
        List<WifiEntity> toAddList = new ArrayList<>();
        //比对扫描到的WiFi和数据库里的WiFi
        for (ScanResult scanResult : wifiList) {
            boolean isAdded = false;
            for (WifiEntity wifiEntity : wifiEntityList) {
                if (scanResult.SSID.replace("\"", "").equals(wifiEntity.getSsid())) {
                    isAdded = true;
                    wifiEntity.setLevel(WifiManager.calculateSignalLevel(scanResult.level, 3));
                    showListwifi.add(wifiEntity);
                    AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                    break;
                }
            }
            if (!isAdded) {
                WifiEntity tempWifiEntity = new WifiEntity();
                tempWifiEntity.setSsid(scanResult.SSID.replace("\"", ""));
                tempWifiEntity.setMacAdrees(scanResult.BSSID);
                tempWifiEntity.setOwnerP2PId("");
                tempWifiEntity.setLevel(WifiManager.calculateSignalLevel(scanResult.level, 3));
                tempWifiEntity.setCapabilities(scanResult.capabilities);
                tempWifiEntity.setIsRegiste(false);
                tempWifiEntity.setGroupNum(-1);
                tempWifiEntity.setFreindNum("");
                KLog.i(tempWifiEntity.toString());
                showListwifi.add(tempWifiEntity);
                toAddList.add(tempWifiEntity);
            }
        }
        AppConfig.getInstance().getDaoSession().getWifiEntityDao().insertInTx(toAddList);
        scanedWifiSsid.addAll(wifiList);
        KLog.i("扫描的WiFi数量为:" + scanedWifiSsid.size());
        if (scanedWifiSsid.size() != 0) {
            getRegistedSsid();
        }
    }

    @Override
    public void clearData() {
        scanedWifiSsid.clear();
    }

    private PermissionListener permission = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == 101) {
                mView.startUpdateWIfiInfo();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 101) {
                KLog.i("权限申请失败");
                ToastUtil.show(((Activity) mView), AppConfig.getInstance().getResources().getString(R.string.permission_denied));
            }
        }
    };

}