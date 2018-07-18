package com.stratagile.qlink.ui.activity.wallet.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.api.HttpObserver;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.FreeNum;
import com.stratagile.qlink.entity.FreeRecord;
import com.stratagile.qlink.entity.MainAddress;
import com.stratagile.qlink.ui.activity.wallet.contract.FreeConnectContract;
import com.stratagile.qlink.ui.activity.wallet.FreeConnectActivity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of FreeConnectActivity
 * @date 2018/07/18 11:53:01
 */
public class FreeConnectPresenter implements FreeConnectContract.FreeConnectContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final FreeConnectContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private FreeConnectActivity mActivity;

    @Inject
    public FreeConnectPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull FreeConnectContract.View view, FreeConnectActivity activity) {
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
    public void zsFreeNum(Map map) {
        httpAPIWrapper.zsFreeNum(map)
                .subscribe(new HttpObserver<FreeNum>() {
                    @Override
                    public void onNext(FreeNum baseBack) {
                        mView.onGetFreeNumBack(baseBack.getData().getFreeNum());
                        onComplete();
                    }
                });
    }

    @Override
    public void queryFreeRecords(Map map) {
        httpAPIWrapper.queryFreeRecords(map)
                .subscribe(new HttpObserver<FreeRecord>() {
                    @Override
                    public void onNext(FreeRecord baseBack) {
                        KLog.i("返回了");
                        mView.onGetFreeRecordBack(baseBack);
                        onComplete();
                    }
                });
    }
}