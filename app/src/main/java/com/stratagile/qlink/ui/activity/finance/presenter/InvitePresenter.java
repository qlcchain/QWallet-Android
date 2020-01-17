package com.stratagile.qlink.ui.activity.finance.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.entity.reward.Dict;
import com.stratagile.qlink.entity.reward.InviteTotal;
import com.stratagile.qlink.entity.reward.InviteeList;
import com.stratagile.qlink.entity.reward.RewardTotal;
import com.stratagile.qlink.entity.topup.SalePartner;
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

    public void getInviteeList(Map map) {
        Disposable disposable = httpAPIWrapper.getInviteeList(map)
                .subscribe(new Consumer<InviteeList>() {
                    @Override
                    public void accept(InviteeList user) throws Exception {
                        //isSuccesse
                        //mView.closeProgressDialog();
                        mView.setInviteeList(user);
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

    public void getRewardList1(Map map) {
        Disposable disposable = httpAPIWrapper.getRewardList1(map)
                .subscribe(new Consumer<SalePartner>() {
                    @Override
                    public void accept(SalePartner user) throws Exception {
                        //isSuccesse
                        //mView.closeProgressDialog();
                        mView.setSalePartner(user);
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

    public void getCanClaimTotal(Map map) {
        Disposable disposable = httpAPIWrapper.getInviteAmount(map)
                .subscribe(new Consumer<InviteTotal>() {
                    @Override
                    public void accept(InviteTotal user) throws Exception {
                        //isSuccesse
                        //mView.closeProgressDialog();
                        mView.setCanClaimTotal(user);
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
    public void getClaimedTotal(Map map) {
        Disposable disposable = httpAPIWrapper.getInviteAmount(map)
                .subscribe(new Consumer<InviteTotal>() {
                    @Override
                    public void accept(InviteTotal user) throws Exception {
                        //isSuccesse
                        //mView.closeProgressDialog();
                        mView.setClaimedTotal(user);
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

    public void getAtlistInviteFriend(Map map) {
        Disposable disposable = httpAPIWrapper.qurryDict(map)
                .subscribe(new Consumer<Dict>() {
                    @Override
                    public void accept(Dict user) throws Exception {
                        //isSuccesse
                        //mView.closeProgressDialog();
                        mView.setAtlistInviteFriend(user);
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

    public void getOneFriendReward(Map map) {
        Disposable disposable = httpAPIWrapper.qurryDict(map)
                .subscribe(new Consumer<Dict>() {
                    @Override
                    public void accept(Dict user) throws Exception {
                        //isSuccesse
                        //mView.closeProgressDialog();
                        mView.setOneFriendReward(user);
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