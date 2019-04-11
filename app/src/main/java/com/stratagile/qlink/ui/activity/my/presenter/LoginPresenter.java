package com.stratagile.qlink.ui.activity.my.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.my.contract.LoginContract;
import com.stratagile.qlink.ui.activity.my.LoginFragment;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of LoginFragment
 * @date 2019/04/09 11:45:22
 */
public class LoginPresenter implements LoginContract.LoginContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final LoginContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private LoginFragment mFragment;

    @Inject
    public LoginPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull LoginContract.View view, LoginFragment fragment) {
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
    public void login(Map map) {
        Disposable disposable = httpAPIWrapper.userLogin(map)
                .subscribe(new Consumer<Register>() {
                    @Override
                    public void accept(Register baseBack) throws Exception {
                        //isSuccesse
                        mView.closeProgressDialog();
                        mView.loginSuccess(baseBack);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.closeProgressDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.closeProgressDialog();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

}