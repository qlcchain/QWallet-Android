package com.stratagile.qlink.ui.activity.finance.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.ui.activity.finance.contract.InviteContract;
import com.stratagile.qlink.ui.activity.finance.InviteActivity;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: presenter of InviteActivity
 * @date 2019/04/23 15:34:34
 */
public class InvitePresenter implements InviteContract.InviteContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final InviteContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private InviteActivity mActivity;

    @Inject
    public InvitePresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull InviteContract.View view, InviteActivity activity) {
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
    public void getInivteRank(Map map) {
        Disposable disposable = httpAPIWrapper.getInivteTop5(map)
                .subscribe(new Consumer<InviteList>() {
                    @Override
                    public void accept(InviteList user) throws Exception {
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