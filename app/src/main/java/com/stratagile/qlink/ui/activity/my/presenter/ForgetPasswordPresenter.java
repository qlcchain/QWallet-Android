package com.stratagile.qlink.ui.activity.my.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.ui.activity.my.contract.ForgetPasswordContract;
import com.stratagile.qlink.ui.activity.my.ForgetPasswordActivity;
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
 * @Description: presenter of ForgetPasswordActivity
 * @date 2019/04/25 10:28:27
 * 修改已经登录的用户的密码
 */
public class ForgetPasswordPresenter implements ForgetPasswordContract.ForgetPasswordContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final ForgetPasswordContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private ForgetPasswordActivity mActivity;

    @Inject
    public ForgetPasswordPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull ForgetPasswordContract.View view, ForgetPasswordActivity activity) {
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
    public void getForgetPasswordVcode(Map map) {
        Disposable disposable = httpAPIWrapper.getForgetPasswordVcode(map)
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