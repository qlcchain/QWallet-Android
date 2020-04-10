package com.stratagile.qlink.ui.activity.my.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.my.contract.LoginContract;
import com.stratagile.qlink.ui.activity.my.LoginActivity;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of LoginActivity
 * @date 2019/04/23 10:05:31
 */
public class LoginPresenter implements LoginContract.LoginContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final LoginContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private LoginActivity mActivity;

    @Inject
    public LoginPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull LoginContract.View view, LoginActivity activity) {
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

    @Override
    public void vCodeLogin(Map map) {
        Disposable disposable = httpAPIWrapper.vCodeLogin(map)
                .subscribe(new Consumer<VcodeLogin>() {
                    @Override
                    public void accept(VcodeLogin baseBack) throws Exception {
                        //isSuccesse
                        mView.vCodeLoginSuccess(baseBack);
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

    @Override
    public void getSignInVcode(Map map) {
        Disposable disposable = httpAPIWrapper.getSignInVcode(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack baseBack) throws Exception {
                        //isSuccesse
                        mView.closeProgressDialog();
                        ToastUtil.displayShortToast(AppConfig.instance.getString(R.string.success));

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