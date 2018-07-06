package com.stratagile.qlink.ui.activity.wifi.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.ui.activity.wifi.contract.RegisterWifiContract;
import com.stratagile.qlink.ui.activity.wifi.RegisterWifiActivity;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: presenter of RegisterWifiActivity
 * @date 2018/01/09 17:28:09
 */
public class RegisterWifiPresenter implements RegisterWifiContract.RegisterWifiContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final RegisterWifiContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private RegisterWifiActivity mActivity;

    @Inject
    public RegisterWifiPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull RegisterWifiContract.View view, RegisterWifiActivity activity) {
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
    public void registWIfi(Map map) {
        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.registeWWifi(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack user) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.closeProgressDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
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