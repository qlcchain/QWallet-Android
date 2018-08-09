package com.stratagile.qlink.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.socks.library.KLog;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.BroadCastAction;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.TransactionRecordDao;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.VpnEntityDao;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.FreeNum;
import com.stratagile.qlink.entity.eventbus.FreeCount;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.utils.CountDownTimerUtils;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.VpnUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by huzhipeng on 2018/2/11.
 */

public class ClientVpnService extends Service {
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    private static CountDownTimerUtils countDownTimerUtils;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.i("vpn计时扣费服务启动了");
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.stratagile.qlink.VPN_STATUS");
        registerReceiver(myBroadcastReceiver, filter);
        int timeInterval = 60 * 60 * 1000;
        if (countDownTimerUtils == null) {
            countDownTimerUtils = CountDownTimerUtils.creatNewInstance();
            countDownTimerUtils.setMillisInFuture(Long.MAX_VALUE)
                    .setCountDownInterval(1 * 60 * 1000)
                    .setTickDelegate(new CountDownTimerUtils.TickDelegate() {
                        @Override
                        public void onTick(long pMillisUntilFinished) {
                            KLog.i("vpn计时扣费"+AppConfig.currentUseVpn);
                            String vpnP2pId = "";
                            if(AppConfig.currentUseVpn !=null)
                            {
                                vpnP2pId = AppConfig.currentUseVpn.getP2pIdPc() == null ? AppConfig.currentUseVpn.getP2pId() : AppConfig.currentUseVpn.getP2pIdPc();
                            }
                            if (AppConfig.currentUseVpn != null && AppConfig.currentUseVpn.getIsConnected() == true && !vpnP2pId.equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
                                if(ConstantValue.freeNum <= 0)
                                {
                                    TransactionRecordDao transactionRecordDao = AppConfig.getInstance().getDaoSession().getTransactionRecordDao();
                                    boolean isMainNet = AppConfig.currentUseVpn.getIsMainNet();
                                    List<TransactionRecord> transactionVpnRecordList = transactionRecordDao.queryBuilder().where(TransactionRecordDao.Properties.AssetName.eq(AppConfig.currentUseVpn.getVpnName()),TransactionRecordDao.Properties.IsMainNet.eq(isMainNet)).list();
                                    if (transactionVpnRecordList.size() > 0) {
                                        Collections.sort(transactionVpnRecordList);
                                        TransactionRecord nearestRecord = transactionVpnRecordList.get(0);
                                        long lastPayTime = nearestRecord.getTimestamp();
                                        KLog.i("vpn计时扣费"+lastPayTime);
                                        if ((Calendar.getInstance().getTimeInMillis() - lastPayTime) > timeInterval)//如果离上次付费超过计费周期，才扣费。vpn是按小时计费
                                        {
                                            KLog.i("vpn计时扣费开始"+lastPayTime);
                                            if(VpnUtil.isInSameNet(AppConfig.currentUseVpn))
                                            {
                                                connectToVpnRecord(AppConfig.currentUseVpn);
                                            }else{
                                                AppConfig.currentUseVpn = null;
                                                Intent intent = new Intent();
                                                intent.setAction(BroadCastAction.disconnectVpn);
                                                sendBroadcast(intent);
                                            }

                                        }
                                    } else {
                                        if(VpnUtil.isInSameNet(AppConfig.currentUseVpn))
                                        {
                                            connectToVpnRecord(AppConfig.currentUseVpn);
                                        }else{
                                            AppConfig.currentUseVpn = null;
                                            Intent intent = new Intent();
                                            intent.setAction(BroadCastAction.disconnectVpn);
                                            sendBroadcast(intent);
                                        }

                                    }
                                }else{
                                    VpnEntityDao vpnEntityDao = AppConfig.getInstance().getDaoSession().getVpnEntityDao();
                                    boolean isMainNet = AppConfig.currentUseVpn.getIsMainNet();
                                    List<VpnEntity> VpnEntityList = vpnEntityDao.queryBuilder().where(VpnEntityDao.Properties.VpnName.eq(AppConfig.currentUseVpn.getVpnName()),VpnEntityDao.Properties.IsMainNet.eq(isMainNet)).list();
                                    if (VpnEntityList.size() > 0) {
                                        long lastFreeTime = VpnEntityList.get(0).getLastFreeTime();
                                        KLog.i("vpn计时免费次数"+lastFreeTime);
                                        if ((Calendar.getInstance().getTimeInMillis() - lastFreeTime) > timeInterval)//如果离上次付费超过计费周期，才扣费。vpn是按小时计费
                                        {
                                            KLog.i("vpn计时免费次数开始"+lastFreeTime);
                                            if(VpnUtil.isInSameNet(AppConfig.currentUseVpn))
                                            {
                                                connectToVpnFree(AppConfig.currentUseVpn);
                                            }else{
                                                AppConfig.currentUseVpn = null;
                                                Intent intent = new Intent();
                                                intent.setAction(BroadCastAction.disconnectVpn);
                                                sendBroadcast(intent);
                                            }

                                        }
                                    }

                                }

                            }
                            //KLog.i("VPN倒计时");
                        }
                    }).start();
        } else {
            countDownTimerUtils.doOnce();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        public static final String TAG = "MyBroadcastReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.stratagile.qlink.VPN_STATUS".equals(intent.getAction())) {
                KLog.i(intent.getStringExtra("detailstatus"));
                KLog.i(intent.getStringExtra("profileuuid"));
                KLog.i(ConstantValue.P2PID);
                KLog.i(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "获取containValue失败"));
                KLog.i(AppConfig.currentVpnUseType);
                if (AppConfig.currentVpnUseType == 1) {
                    KLog.i("当前为收费模式的连接");
                }
            }
        }
    }
    public void connectToVpnRecord(VpnEntity vpnEntity) {
        LogUtil.addLog("计时器开始发起扣款申请", getClass().getSimpleName());
        KLog.i("vpn连接成功，计时器开始发起扣款申请");
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().queryBuilder().list();
        if (walletList == null || walletList.size() == 0 || walletList.size() <= SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0)) {
            SpUtil.putInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0);
        }
        Wallet wallet = walletList.get(SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0));
        Map<String, Object> infoMap = new HashMap<>();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String uuid1 = uuid.substring(0, 32);
        infoMap.put("recordId", uuid1);
        infoMap.put("assetName", vpnEntity.getVpnName());
        infoMap.put("type", 3);
        infoMap.put("addressFrom", wallet.getAddress());
        infoMap.put("addressTo", vpnEntity.getAddress());
        infoMap.put("qlc", vpnEntity.getQlc() + "");
        infoMap.put("fromP2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
        infoMap.put("toP2pId", vpnEntity.getP2pId());
        if(vpnEntity.getQlc() <=0)
        {
            return;
        }
        TransactionApi.getInstance().v2Transaction(infoMap, wallet.getAddress(), vpnEntity.getAddress(), vpnEntity.getQlc() + "", new SendBackWithTxId() {
            @Override
            public void onSuccess(String txid) {
                TransactionRecord recordSave = new TransactionRecord();
                recordSave.setTxid(txid);
                recordSave.setExChangeId(txid);
                recordSave.setAssetName(vpnEntity.getVpnName());
                recordSave.setTransactiomType(3);
                recordSave.setIsReported(false);
                recordSave.setConnectType(0);
                recordSave.setFriendNum(vpnEntity.getFriendNum());
                recordSave.setQlcCount(vpnEntity.getQlc());
                recordSave.setTimestamp(Calendar.getInstance().getTimeInMillis());
                recordSave.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
                AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(recordSave);
                Qsdk.getInstance().sendRecordSaveReq(vpnEntity.getFriendNum(), recordSave);
            }

            @Override
            public void onFailure() {
                //ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.deductions_failure));
            }
        });
    }
    public void connectToVpnFree(VpnEntity vpnEntity) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("assetName", vpnEntity.getVpnName());
        infoMap.put("fromP2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
        infoMap.put("toP2pId", vpnEntity.getP2pId());
        infoMap.put("addressTo", vpnEntity.getAddress());
        AppConfig.getInstance().getApplicationComponent().getHttpApiWrapper().freeConnection(infoMap)
                .subscribe(new Observer<FreeNum>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(FreeNum freeNum) {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        ConstantValue.freeNum = freeNum.getData().getFreeNum();
                        EventBus.getDefault().post(new FreeCount(freeNum.getData().getFreeNum()));
                        vpnEntity.setLastFreeTime(new Date().getTime());
                        AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                        disposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.i("onError");
                        e.printStackTrace();
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        KLog.i("onComplete");
                        disposable.dispose();
                    }
                });
    }
}
