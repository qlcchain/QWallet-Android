package com.stratagile.qlink.ui.activity.finance.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.newwinq.Product;
import com.stratagile.qlink.ui.activity.finance.contract.FinanceContract;
import com.stratagile.qlink.ui.activity.finance.FinanceFragment;
import com.stratagile.qlink.utils.DigestUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: presenter of FinanceFragment
 * @date 2019/04/08 17:36:49
 */
public class FinancePresenter implements FinanceContract.FinanceContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final FinanceContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private FinanceFragment mFragment;

    @Inject
    public FinancePresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull FinanceContract.View view, FinanceFragment fragment) {
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
    public void getProductList(Map map) {
        Disposable disposable = httpAPIWrapper.getProductList(map)
                .subscribe(new Consumer<Product>() {
                    @Override
                    public void accept(Product baseBack) throws Exception {
                        //isSuccesse
                        mView.closeProgressDialog();
                        mView.getProductBack(baseBack);
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