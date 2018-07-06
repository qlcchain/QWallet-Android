package com.stratagile.qlink.ui.activity.wifi.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.RegisterWiFi;
import com.stratagile.qlink.entity.VertifyVpn;
import com.stratagile.qlink.ui.activity.wifi.contract.WifiRegisteContract;
import com.stratagile.qlink.ui.activity.wifi.WifiRegisteFragment;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.Calendar;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: presenter of WifiRegisteFragment
 * @date 2018/01/15 11:52:32
 */
public class WifiRegistePresenter implements WifiRegisteContract.WifiRegisteContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final WifiRegisteContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private WifiRegisteFragment mFragment;

    @Inject
    public WifiRegistePresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull WifiRegisteContract.View view, WifiRegisteFragment fragment) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mFragment = fragment;
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
    public void registeWifi(Map map, String ssid) {
        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.registeWWifiV3(map)
                .subscribe(new Consumer<RegisterWiFi>() {
                    @Override
                    public void accept(RegisterWiFi result) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        ToastUtil.displayShortToast(result.getMsg());
                        mView.closeProgressDialog();
                        mView.registAssetSuccess();
                        TransactionRecord recordSave = new TransactionRecord();
                        recordSave.setTxid(result.getRecordId());
                        recordSave.setExChangeId(result.getRecordId());
                        recordSave.setAssetName(ssid);
                        recordSave.setTransactiomType(4);
                        recordSave.setTimestamp(Calendar.getInstance().getTimeInMillis());
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
    @Override
    public void updateWiFiInfo(Map map, String ssid) {
        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.updateWiFiInfo(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack result) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.update_success));
                        mView.closeProgressDialog();
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
    @Override
    public void getBalance(Map map) {
        Disposable disposable = httpAPIWrapper.getBalance(map)
                .subscribe(new Consumer<Balance>() {
                    @Override
                    public void accept(Balance balance) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.onGetBalancelSuccess(balance);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
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

    @Override
    public void vertifyWiFiName(Map map) {
        Disposable disposable = httpAPIWrapper.vertifyVpnName(map)
                .subscribe(new Consumer<VertifyVpn>() {
                    @Override
                    public void accept(VertifyVpn vertifyVpn) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.vertifyWiFiBack(vertifyVpn);
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