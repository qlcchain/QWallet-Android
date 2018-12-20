package com.stratagile.qlink.ui.activity.eos.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.eos.EosResourcePrice;
import com.stratagile.qlink.ui.activity.eos.contract.EosActivationContract;
import com.stratagile.qlink.ui.activity.eos.EosActivationActivity;
import com.stratagile.qlink.utils.SpUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.eblock.eos4j.Ecc;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: presenter of EosActivationActivity
 * @date 2018/12/12 11:17:52
 */
public class EosActivationPresenter implements EosActivationContract.EosActivationContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final EosActivationContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private EosActivationActivity mActivity;

    @Inject
    public EosActivationPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull EosActivationContract.View view, EosActivationActivity activity) {
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
    public void getEosResourcePrice(Map map) {
        Disposable disposable = httpAPIWrapper.getEosResourcePrice(map)
                .subscribe(new Consumer<EosResourcePrice>() {
                    @Override
                    public void accept(EosResourcePrice baseBack) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.setEosResourcePrice(baseBack);
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
}