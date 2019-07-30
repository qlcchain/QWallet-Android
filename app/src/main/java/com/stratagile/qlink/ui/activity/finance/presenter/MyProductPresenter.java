package com.stratagile.qlink.ui.activity.finance.presenter;

import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.newwinq.Order;
import com.stratagile.qlink.entity.newwinq.ProductDetail;
import com.stratagile.qlink.ui.activity.finance.contract.MyProductContract;
import com.stratagile.qlink.ui.activity.finance.MyProductActivity;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: presenter of MyProductActivity
 * @date 2019/04/11 16:18:23
 */
public class MyProductPresenter implements MyProductContract.MyProductContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final MyProductContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private MyProductActivity mActivity;

    @Inject
    public MyProductPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull MyProductContract.View view, MyProductActivity activity) {
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
    public void getOrderList(Map map) {
        Disposable disposable = httpAPIWrapper.getOrderList(map)
                .subscribe(new Consumer<Order>() {
                    @Override
                    public void accept(Order baseBack) throws Exception {
                        //isSuccesse
                        mView.closeProgressDialog();
                        mView.getOrderBack(baseBack);
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
    public void redeemOrder(Map map) {
        Disposable disposable = httpAPIWrapper.redeemOrder(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack unspent) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.redeemOrderBack(unspent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
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