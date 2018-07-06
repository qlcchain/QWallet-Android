package com.stratagile.qlink.ui.activity.wordcup.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.data.api.MainHttpAPIWrapper;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.BetResult;
import com.stratagile.qlink.ui.activity.wordcup.contract.WordCupBetContract;
import com.stratagile.qlink.ui.activity.wordcup.WordCupBetActivity;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: presenter of WordCupBetActivity
 * @date 2018/06/12 11:36:18
 */
public class WordCupBetPresenter implements WordCupBetContract.WordCupBetContractPresenter{

    MainHttpAPIWrapper httpAPIWrapper;
    private final WordCupBetContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private WordCupBetActivity mActivity;

    @Inject
    public WordCupBetPresenter(@NonNull MainHttpAPIWrapper httpAPIWrapper, @NonNull WordCupBetContract.View view, WordCupBetActivity activity) {
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
    public void betRace(Map map) {
        Disposable disposable = httpAPIWrapper.betRace(map)
                .subscribe(new Consumer<BetResult>() {
                    @Override
                    public void accept(BetResult betResult) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        if (betResult.getData().isBetResult()) {
                            ToastUtil.displayShortToast("bet success");
                            mView.onBetSuccess(betResult.getData().getRaceInfo());
                        } else {
                            ToastUtil.displayShortToast("bet failure");
                            mView.closeProgressDialog();
                        }
                        //mView.closeProgressDialog();
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

}