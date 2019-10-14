package com.stratagile.qlink.ui.activity.my.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.my.contract.Login1Contract;
import com.stratagile.qlink.ui.activity.my.Login1Fragment;
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
 * @Description: presenter of Login1Fragment
 * @date 2019/04/24 18:02:10
 */
public class Login1Presenter implements Login1Contract.Login1ContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final Login1Contract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private Login1Fragment mFragment;

    @Inject
    public Login1Presenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull Login1Contract.View view, Login1Fragment fragment) {
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
                        mView.loginError("登录错误1" + throwable.toString());
                        KLog.e(throwable.getMessage());
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.closeProgressDialog();
                        mView.loginError("登录错误2");
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
                        mView.getLoginVCodeSuccess();
                        ToastUtil.displayShortToast("success");

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