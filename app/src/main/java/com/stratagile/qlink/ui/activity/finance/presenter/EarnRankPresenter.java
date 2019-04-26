package com.stratagile.qlink.ui.activity.finance.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.finance.EarnRank;
import com.stratagile.qlink.entity.finance.MyRanking;
import com.stratagile.qlink.ui.activity.finance.contract.EarnRankContract;
import com.stratagile.qlink.ui.activity.finance.EarnRankActivity;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: presenter of EarnRankActivity
 * @date 2019/04/24 11:27:01
 * 富豪榜页面
 */
public class EarnRankPresenter implements EarnRankContract.EarnRankContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final EarnRankContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private EarnRankActivity mActivity;

    @Inject
    public EarnRankPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull EarnRankContract.View view, EarnRankActivity activity) {
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
    public void getEarnRank(Map map) {
        Disposable disposable = httpAPIWrapper.getEarnRankings(map)
                .subscribe(new Consumer<EarnRank>() {
                    @Override
                    public void accept(EarnRank user) throws Exception {
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