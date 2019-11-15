package com.stratagile.qlink.ui.activity.otc.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.EntrustOrderList;
import com.stratagile.qlink.entity.NeoWalletInfo;
import com.stratagile.qlink.entity.newwinq.MiningAct;
import com.stratagile.qlink.entity.newwinq.Order;
import com.stratagile.qlink.entity.otc.TradePair;
import com.stratagile.qlink.ui.activity.otc.contract.MarketContract;
import com.stratagile.qlink.ui.activity.otc.MarketFragment;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.market
 * @Description: presenter of MarketFragment
 * @date 2019/06/14 16:23:19
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

    public void getPairs() {
        Disposable disposable = httpAPIWrapper.getPairs(new HashMap<String, String>())
                .subscribe(new Consumer<TradePair>() {
                    @Override
                    public void accept(TradePair baseBack) throws Exception {
                        //isSuccesse
                        mView.setRemoteTradePairs(baseBack.getPairsList());
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

    public void getMiningList() {
        Disposable disposable = httpAPIWrapper.miningList(new HashMap<String, String>())
                .subscribe(new Consumer<MiningAct>() {
                    @Override
                    public void accept(MiningAct miningAct) throws Exception {
                        //isSuccesse
                        mView.setMiningAct(miningAct);
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