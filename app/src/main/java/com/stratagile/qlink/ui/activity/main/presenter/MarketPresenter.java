package com.stratagile.qlink.ui.activity.main.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.EthWalletInfo;
import com.stratagile.qlink.entity.Tpcs;
import com.stratagile.qlink.ui.activity.main.contract.MarketContract;
import com.stratagile.qlink.ui.activity.main.MarketFragment;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: presenter of MarketFragment
 * @date 2018/10/25 15:54:02
 */
public class MarketPresenter implements MarketContract.MarketContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final MarketContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private MarketFragment mFragment;

    @Inject
    public MarketPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull MarketContract.View view, MarketFragment fragment) {
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
    public void getTpcs(Map map) {
        Disposable disposable = httpAPIWrapper.getTpcs(map)
                .subscribe(new Consumer<Tpcs>() {
                    @Override
                    public void accept(Tpcs baseBack) throws Exception {
                        //isSuccesse
                        mView.setData(baseBack);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

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