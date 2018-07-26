package com.stratagile.qlink.ui.activity.seize.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.entity.RegisterVpn;
import com.stratagile.qlink.entity.RegisterWiFi;
import com.stratagile.qlink.ui.activity.seize.contract.SeizeVpnContract;
import com.stratagile.qlink.ui.activity.seize.SeizeVpnActivity;
import com.stratagile.qlink.utils.SpUtil;

import java.util.Calendar;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.seize
 * @Description: presenter of SeizeVpnActivity
 * @date 2018/04/13 12:08:31
 */
public class SeizeVpnPresenter implements SeizeVpnContract.SeizeVpnContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final SeizeVpnContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private SeizeVpnActivity mActivity;

    @Inject
    public SeizeVpnPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull SeizeVpnContract.View view, SeizeVpnActivity activity) {
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
    }

    @Override
    public void registerVpn(Map map, String vpnName) {

//        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.vpnRegisterV2(map)
                .subscribe(new Consumer<RegisterVpn>() {
                    @Override
                    public void accept(RegisterVpn result) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.closeProgressDialog();
                        mView.registVpnSuccess();
                        TransactionRecord recordSave = new TransactionRecord();
                        recordSave.setTxid(result.getRecordId());
                        recordSave.setExChangeId(result.getRecordId());
                        recordSave.setAssetName(vpnName);
                        recordSave.setTransactiomType(5);
                        recordSave.setTimestamp(Calendar.getInstance().getTimeInMillis());
                        recordSave.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
                        AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(recordSave);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.closeProgressDialog();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

}