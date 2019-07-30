package com.stratagile.qlink.ui.activity.finance.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.entity.finance.MyRanking;
import com.stratagile.qlink.ui.activity.finance.contract.MyRankingContract;
import com.stratagile.qlink.ui.activity.finance.MyRankingActivity;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: presenter of MyRankingActivity
 * @date 2019/04/24 11:14:23
 */
public class MyRankingPresenter implements MyRankingContract.MyRankingContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final MyRankingContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private MyRankingActivity mActivity;

    @Inject
    public MyRankingPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull MyRankingContract.View view, MyRankingActivity activity) {
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
    public void getRanking(Map map) {
        Disposable disposable = httpAPIWrapper.getRankings(map)
                .subscribe(new Consumer<MyRanking>() {
                    @Override
                    public void accept(MyRanking user) throws Exception {
                        //isSuccesse
                        //mView.closeProgressDialog();
                        mView.setData(user);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
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