package com.stratagile.qlink.ui.activity.my.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.entity.UserInfo;
import com.stratagile.qlink.ui.activity.my.contract.PersonContract;
import com.stratagile.qlink.ui.activity.my.PersonActivity;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of PersonActivity
 * @date 2019/04/22 14:28:46
 */
public class PersonPresenter implements PersonContract.PersonContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final PersonContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private PersonActivity mActivity;

    @Inject
    public PersonPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull PersonContract.View view, PersonActivity activity) {
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
    public void changeNickName(Map map) {
        Disposable disposable = httpAPIWrapper.changeNickName(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack user) throws Exception {
                        //isSuccesse
                        //mView.closeProgressDialog();
                        mView.changeNickNameBack(user);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
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

}