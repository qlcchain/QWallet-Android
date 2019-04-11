package com.stratagile.qlink.ui.activity.my.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.my.contract.RegiesterContract;
import com.stratagile.qlink.ui.activity.my.RegiesterFragment;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: presenter of RegiesterFragment
 * @date 2019/04/09 11:45:07
 */
public class RegiesterPresenter implements RegiesterContract.RegiesterContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final RegiesterContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private RegiesterFragment mFragment;

    @Inject
    public RegiesterPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull RegiesterContract.View view, RegiesterFragment fragment) {
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
    public void getSignUpVcode(Map map) {
        Disposable disposable = httpAPIWrapper.getSignUpVcode(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack baseBack) throws Exception {
                        //isSuccesse
                        mView.closeProgressDialog();

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
    public void register(Map map) {
        Disposable disposable = httpAPIWrapper.userRegister(map)
                .subscribe(new Consumer<Register>() {
                    @Override
                    public void accept(Register baseBack) throws Exception {
                        //isSuccesse
                        mView.closeProgressDialog();
                        mView.registerSuccess(baseBack);
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