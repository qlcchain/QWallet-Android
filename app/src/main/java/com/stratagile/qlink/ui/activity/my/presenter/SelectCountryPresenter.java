package com.stratagile.qlink.ui.activity.my.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.GotWinqGas;
import com.stratagile.qlink.ui.activity.my.contract.SelectCountryContract;
import com.stratagile.qlink.ui.activity.my.SelectCountryActivity;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of SelectCountryActivity
 * @date 2019/04/09 14:38:53
 */
public class SelectCountryPresenter implements SelectCountryContract.SelectCountryContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final SelectCountryContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private SelectCountryActivity mActivity;

    @Inject
    public SelectCountryPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull SelectCountryContract.View view, SelectCountryActivity activity) {
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
    public void getSignUpVcode(Map map) {
        Disposable disposable = httpAPIWrapper.getSignUpVcode(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack baseBack) throws Exception {
                        //isSuccesse
                        mView.closeProgressDialog();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
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