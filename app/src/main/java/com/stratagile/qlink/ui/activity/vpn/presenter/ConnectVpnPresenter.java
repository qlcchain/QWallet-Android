package com.stratagile.qlink.ui.activity.vpn.presenter;

import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.api.HttpObserver;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.ui.activity.vpn.contract.ConnectVpnContract;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: presenter of ConnectVpnActivity
 * @date 2018/02/08 16:38:02
 */
public class ConnectVpnPresenter implements ConnectVpnContract.ConnectVpnContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private ConnectVpnContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public ConnectVpnPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull ConnectVpnContract.View view) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        mView = null;
    }

    @Override
    public void connectToVpnRecord(VpnEntity vpnEntity, boolean upUI) {
        LogUtil.addLog("vpn连接成功，开始发起扣款申请", getClass().getSimpleName());
        KLog.i("vpn连接成功，开始发起扣款申请");
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().queryBuilder().list();
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
        TransactionApi.getInstance().v2Transaction(infoMap, wallet.getAddress(), vpnEntity.getAddress(), vpnEntity.getQlc() + "", new SendBackWithTxId() {
            @Override
            public void onSuccess(String txid) {
                mView.onRecordSuccess(upUI);
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
                AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(recordSave);
            }

            @Override
            public void onFailure() {
                ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.deductions_failure));
            }
        });
    }

    @Override
    public void getBalance(Map map) {
        httpAPIWrapper.getBalance(map)
                .subscribe(new HttpObserver<Balance>() {
                    @Override
                    public void onNext(Balance balance) {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.onGetBalancelSuccess(balance);
                    }
                });
    }
}