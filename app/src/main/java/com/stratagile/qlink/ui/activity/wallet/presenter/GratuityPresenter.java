package com.stratagile.qlink.ui.activity.wallet.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.ui.activity.wallet.contract.GratuityContract;
import com.stratagile.qlink.ui.activity.wallet.GratuityActivity;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of GratuityActivity
 * @date 2018/02/02 16:19:02
 */
public class GratuityPresenter implements GratuityContract.GratuityContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final GratuityContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private GratuityActivity mActivity;

    @Inject
    public GratuityPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull GratuityContract.View view, GratuityActivity activity) {
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
    public void getBalance(Map map) {
        Disposable disposable = httpAPIWrapper.getBalance(map)
                .subscribe(new Consumer<Balance>() {
                    @Override
                    public void accept(Balance balance) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.onGetBalancelSuccess(balance);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
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

    @Override
    public void reward(Map map, Wallet wallet, String qlc, String toAddress) {
        mView.showProgressDialog();
        TransactionApi.getInstance().v2Transaction(map, wallet.getAddress(), toAddress, qlc +"", new SendBackWithTxId() {
            @Override
            public void onSuccess(String txid) {
                mView.closeProgressDialog();
                mView.rewardBack(null);
            }

            @Override
            public void onFailure() {
                mView.closeProgressDialog();
                ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.gratuity_failure));
            }
        });
//        Disposable disposable = httpAPIWrapper.reward(map)
//                .subscribe(new Consumer<Reward>() {
//                    @Override
//                    public void accept(Reward reward) throws Exception {
//                        //isSuccesse
//                        KLog.i("onSuccesse");
//                        mView.closeProgressDialog();
//                        mView.rewardBack(reward);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        //onError
//                        KLog.i("onError");
//                        throwable.printStackTrace();
//                        mView.closeProgressDialog();
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        //onComplete
//                        KLog.i("onComplete");
//                        mView.closeProgressDialog();
//                    }
//                });
//        mCompositeDisposable.add(disposable);
    }
}