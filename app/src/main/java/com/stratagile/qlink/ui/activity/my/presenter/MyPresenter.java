package com.stratagile.qlink.ui.activity.my.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.UserInfo;
import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.reward.InviteTotal;
import com.stratagile.qlink.entity.reward.RewardTotal;
import com.stratagile.qlink.ui.activity.my.contract.MyContract;
import com.stratagile.qlink.ui.activity.my.MyFragment;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of MyFragment
 * @date 2019/04/09 10:02:03
 */
public class MyPresenter implements MyContract.MyContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final MyContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private MyFragment mFragment;

    @Inject
    public MyPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull MyContract.View view, MyFragment fragment) {
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

    public void getUserInfo(Map map) {
        mCompositeDisposable.add(httpAPIWrapper.getUserInfo(map).subscribe(new Consumer<UserInfo>() {
            @Override
            public void accept(UserInfo user) throws Exception {
                //isSuccesse
                mView.setUsrInfo(user);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //onError
                throwable.printStackTrace();
                //mView.closeProgressDialog();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //onComplete
            }
        }));
    }


    public void getCanClaimTotal(HashMap<String, String> map) {
        mCompositeDisposable.add(httpAPIWrapper.getRewardTotal(map).subscribe(new Consumer<RewardTotal>() {
            @Override
            public void accept(RewardTotal rewardTotal) throws Exception {
                mView.setCanClaimTotal(rewardTotal);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //onError
                throwable.printStackTrace();
                //mView.closeProgressDialog();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //onComplete
            }
        }));
    }
    public void getCanInviteClaimTotal(Map<String, String> map) {
        mCompositeDisposable.add(httpAPIWrapper.getInviteAmount(map).subscribe(new Consumer<InviteTotal>() {
            @Override
            public void accept(InviteTotal rewardTotal) throws Exception {
                mView.setCanInviteClaimTotal(rewardTotal);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                //onError
                throwable.printStackTrace();
                //mView.closeProgressDialog();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //onComplete
            }
        }));
    }

}