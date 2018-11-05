package com.stratagile.qlink.ui.activity.wallet.presenter;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.ui.activity.eth.EthWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.SelectWalletTypeContract;
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of SelectWalletTypeActivity
 * @date 2018/10/19 10:51:45
 */
public class SelectWalletTypePresenter implements SelectWalletTypeContract.SelectWalletTypeContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final SelectWalletTypeContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private SelectWalletTypeActivity mActivity;

    @Inject
    public SelectWalletTypePresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull SelectWalletTypeContract.View view, SelectWalletTypeActivity activity) {
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
    public void createEthWallet() {
        mView.showProgressDialog();
        Observable.create(new ObservableOnSubscribe<EthWallet>() {
            @Override
            public void subscribe(ObservableEmitter<EthWallet> emitter) throws Exception {
                EthWallet ethWallet = ETHWalletUtils.generateMnemonic();
                AppConfig.getInstance().getDaoSession().getEthWalletDao().insert(ethWallet);
                KLog.i(ethWallet.getMnemonic());
                emitter.onNext(ethWallet);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EthWallet>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EthWallet ethWallet) {
                        mView.createEthWalletSuccess(ethWallet);
                        mView.closeProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

//    @Override
//    public void getUser(HashMap map) {
//        //mView.showProgressDialog();
//        Disposable disposable = httpAPIWrapper.getUser(map)
//                .subscribe(new Consumer<User>() {
//                    @Override
//                    public void accept(User user) throws Exception {
//                        //isSuccesse
//                        KLog.i("onSuccesse");
//                        mView.setText(user);
//                      //mView.closeProgressDialog();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        //onError
//                        KLog.i("onError");
//                        throwable.printStackTrace();
//                      //mView.closeProgressDialog();
//                      //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        //onComplete
//                        KLog.i("onComplete");
//                    }
//                });
//        mCompositeDisposable.add(disposable);
//    }
}