package com.stratagile.qlink.ui.activity.eos.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.EosResource;
import com.stratagile.qlink.ui.activity.eos.contract.EosBuyRamContract;
import com.stratagile.qlink.ui.activity.eos.EosBuyRamActivity;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: presenter of EosBuyRamActivity
 * @date 2018/12/06 14:39:06
 */
public class EosBuyRamPresenter implements EosBuyRamContract.EosBuyRamContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final EosBuyRamContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private EosBuyRamActivity mActivity;

    @Inject
    public EosBuyRamPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull EosBuyRamContract.View view, EosBuyRamActivity activity) {
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
    public void getEosResource(Map map) {
        Disposable disposable = httpAPIWrapper.getEosTResource(map)
                .subscribe(new Consumer<EosResource>() {
                    @Override
                    public void accept(EosResource baseBack) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.setEosResource(baseBack);
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