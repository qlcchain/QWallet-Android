package com.stratagile.qlink.ui.activity.wifi.presenter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.stratagile.qlink.utils.SpUtil;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.ConnectedWifiRecord;
import com.stratagile.qlink.entity.wifi.WifipasswordRsp;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.ui.activity.wifi.contract.ConnectWifiConfirmContract;
import com.stratagile.qlink.ui.activity.wifi.ConnectWifiConfirmActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: presenter of ConnectWifiConfirmActivity
 * @date 2018/01/19 19:46:27
 */
public class ConnectWifiConfirmPresenter implements ConnectWifiConfirmContract.ConnectWifiConfirmContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final ConnectWifiConfirmContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private ConnectWifiConfirmActivity mActivity;
    private WifiEntity mConnectWifiEntity;
    List<WifiConfiguration> existingConfigsLeft = new ArrayList<>();
    private int flag = 0;
    private boolean isOverTime = true;
    private int networkid;

    @Inject
    public ConnectWifiConfirmPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull ConnectWifiConfirmContract.View view, ConnectWifiConfirmActivity activity) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mActivity = activity;
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        mConnectWifiEntity = null;
    }

    @Override
    public void createLinkToWifi(WifipasswordRsp wifipasswordRsp) {
        KLog.i("连接WiFi界面接受到的WiFi密码为:" + wifipasswordRsp.getPassword());
        KLog.i("连接WiFi界面接受到的WiFissid为:" + wifipasswordRsp.getSsid());
        List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().list();
        mConnectWifiEntity = null;
        for (WifiEntity wifiEntity : wifiEntityList) {
            if (wifiEntity.getSsid().equals(wifipasswordRsp.getSsid())) {
                mConnectWifiEntity = wifiEntity;
                KLog.i("找到了数据库中保存的需要连接的WiFi了。。。。");
                break;
            }
        }
        WifiManager wifiManager = (WifiManager) AppConfig.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mConnectWifiEntity != null && wifiManager != null) {
            boolean existingConfigHad = false;
            try {
                List<WifiConfiguration> existingConfigs = wifiManager
                        .getConfiguredNetworks();

                for (WifiConfiguration existingConfig : existingConfigs) {//先删除所有的配置，android6.0删除不了
                    if (existingConfig.SSID.equals("\"" + mConnectWifiEntity.getSsid() + "\"")) {
                        wifiManager.removeNetwork(existingConfig.networkId);
                        wifiManager.saveConfiguration();
                    }

                }
                existingConfigsLeft = new ArrayList<>();
                existingConfigs = wifiManager
                        .getConfiguredNetworks();
                for (WifiConfiguration existingConfig : existingConfigs) {
                    if (existingConfig.SSID.equals("\"" + mConnectWifiEntity.getSsid() + "\"")) {
                        existingConfigHad = true;
                        existingConfigsLeft.add(existingConfig);//android6.0删除不掉的剩余
                    }
                }
            }catch (Exception e)
            {

            }
            WifiConfiguration conf = null;
            if(!existingConfigHad)
            {
                conf = new WifiConfiguration();
                conf.SSID = "\"" + mConnectWifiEntity.getSsid() + "\"";
                existingConfigsLeft.add(conf);
            }
            flag= 0;
            doConnectWiFi(existingConfigsLeft,wifipasswordRsp,wifiManager,existingConfigHad,0);
        } else {
            mView.conncetWifiFailure("connect to wifi " + wifipasswordRsp.getSsid() + " failure, please reflash wifi list");
        }
    }

    private void doConnectWiFi(List<WifiConfiguration> existingConfigsLeft,WifipasswordRsp wifipasswordRsp,WifiManager wifiManager,boolean existingConfigHad,int index)
    {
        WifiConfiguration conf = existingConfigsLeft.get(index);
        if (mConnectWifiEntity.getCapabilities().contains("WEP")) {
            conf.wepKeys[0] = "\"" + wifipasswordRsp.getPassword() + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.wepTxKeyIndex = 0;
        } else if (mConnectWifiEntity.getCapabilities().contains("WPA")) {
            conf.preSharedKey = "\"" + wifipasswordRsp.getPassword() + "\"";
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.status = WifiConfiguration.Status.ENABLED;
        } else {
            //conf.wepKeys[0] = "";
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //conf.wepTxKeyIndex = 0;
        }
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        networkid = -1;
        if(!existingConfigHad)
        {
            networkid = wifiManager.addNetwork(conf);
        }else{
            wifiManager.updateNetwork(conf);
            networkid = conf.networkId;
        }
        KLog.i("networkid"+networkid);
        if(networkid == -1)
        {
            mView.conncetWifiFailure("connect to wifi " + wifipasswordRsp.getSsid() + " failure,please try again!");
            return;
        }
        wifiManager.disconnect();
        new Thread(new Runnable() {
            @Override
            public void run() {
                isOverTime = true;
                boolean flagNet = wifiManager.enableNetwork(networkid, true);
            }
        }).start();
        //wifiManager.reconnect();
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connManager = (ConnectivityManager) mView.getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                  if (connManager != null) {
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (mWifi.isConnected() && wifiManager.getConnectionInfo().getSSID().replace("\"", "").equals(mConnectWifiEntity.getSsid())) {
                        KLog.i("wifi通过qlink连接成功~~~");
                            connectToWifiRecord(wifipasswordRsp);
                        mConnectWifiEntity.setIsConnected(true);
                        AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(mConnectWifiEntity);
                        EventBus.getDefault().post(new ArrayList<WifiEntity>());
                        Qsdk.getInstance().wifiConnectSuccessReq(wifipasswordRsp.getFriendNum(), wifipasswordRsp.getSsid());
                    } else {
                        flag ++;
                        if(flag < existingConfigsLeft.size())
                        {
                            doConnectWiFi(existingConfigsLeft,wifipasswordRsp,wifiManager,existingConfigHad,flag);
                        }else{
                            if(isOverTime)
                            {
                                mView.conncetWifiFailure("connect to wifi " + wifipasswordRsp.getSsid() + " overtime, please try again!");
                            }else
                            {
                                mView.conncetWifiFailure("connect to wifi " + wifipasswordRsp.getSsid() + " failure, password error?");
                            }

                        }

                    }
                } else {
                    mView.conncetWifiFailure("connect to wifi " + wifipasswordRsp.getSsid() + " failure, system error.");
                }
            }
        }, 6000);
    }
    public void stopConnectWiFi()
    {
        if(existingConfigsLeft != null)
            flag = existingConfigsLeft.size();
    }
    /**
     * 连接WiFi成功，调用区块链的接口，告诉区块链连接WiFi成功了
     */
    private void connectToWifiRecord(WifipasswordRsp wifipasswordRsp) {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().queryBuilder().list();
        if (mConnectWifiEntity != null) {
            Map<String, Object> map = new HashMap<>();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            uuid = uuid.substring(0, 32);
            KLog.i(uuid);
            map.put("addressFrom", walletList.get(SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0)).getAddress());  //连接人的钱包address
            map.put("addressTo", mConnectWifiEntity.getWalletAddress());    //提供WiFi的人的钱包地址
            map.put("fromP2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
            map.put("qlc", "0");
            map.put("recordId", uuid);
            map.put("wifiName", mConnectWifiEntity.getSsid());
            map.put("toP2pId", mConnectWifiEntity.getOwnerP2PId());
            Disposable disposable = httpAPIWrapper.recordSave(map)
                    .subscribe(new Consumer<ConnectedWifiRecord>() {
                        @Override
                        public void accept(ConnectedWifiRecord result) throws Exception {
                            //isSuccesse
                            KLog.i("onSuccesse");
                            TransactionRecord recordSave = new TransactionRecord();
                            recordSave.setTxid(result.getRecordId());
                            recordSave.setExChangeId(result.getRecordId());
                            recordSave.setAssetName(wifipasswordRsp.getSsid());
                            recordSave.setTransactiomType(0);
                            recordSave.setConnectType(0);
                            recordSave.setIsReported(false);
                            recordSave.setTimestamp(Calendar.getInstance().getTimeInMillis());
                            Qsdk.getInstance().sendRecordSaveReq(wifipasswordRsp.getFriendNum(), recordSave);
                            AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(recordSave);

                            mView.connectWifiSuccess("connect to wifi " + wifipasswordRsp.getSsid() + " success", wifipasswordRsp.getSsid());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            //onError
                            KLog.i("onError");
                            throwable.printStackTrace();
                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            //onComplete
                            KLog.i("onComplete");
                        }
                    });
            mCompositeDisposable.add(disposable);
        }
    }

    @Override
    public void getWifiPassword() {

    }

}