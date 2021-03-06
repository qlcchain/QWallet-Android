package com.stratagile.qlink.ui.activity.otc.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.api.HttpObserver;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.LocalTokenBean;
import com.stratagile.qlink.ui.activity.otc.contract.AddTokenContract;
import com.stratagile.qlink.ui.activity.otc.AddTokenActivity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.market
 * @Description: presenter of AddTokenActivity
 * @date 2018/11/28 11:03:57
 */
public class AddTokenPresenter implements AddTokenContract.AddTokenContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final AddTokenContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private AddTokenActivity mActivity;

    @Inject
    public AddTokenPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull AddTokenContract.View view, AddTokenActivity activity) {
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
    public void getBinaTokens(Map map) {
        httpAPIWrapper.getBinaTokens(new HashMap()).subscribe(new HttpObserver<LocalTokenBean>() {
            @Override
            public void onNext(LocalTokenBean mainAddress) {
                KLog.i("onSuccesse");
                mView.getBinaTokensSuccess(mainAddress);
                onComplete();
            }
        });
    }
}