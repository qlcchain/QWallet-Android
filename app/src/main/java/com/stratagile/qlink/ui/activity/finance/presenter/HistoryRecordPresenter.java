package com.stratagile.qlink.ui.activity.finance.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.finance.HistoryRecord;
import com.stratagile.qlink.entity.finance.MyRanking;
import com.stratagile.qlink.ui.activity.finance.contract.HistoryRecordContract;
import com.stratagile.qlink.ui.activity.finance.HistoryRecordActivity;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: presenter of HistoryRecordActivity
 * @date 2019/04/24 13:48:39
 */
public class HistoryRecordPresenter implements HistoryRecordContract.HistoryRecordContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final HistoryRecordContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private HistoryRecordActivity mActivity;

    @Inject
    public HistoryRecordPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull HistoryRecordContract.View view, HistoryRecordActivity activity) {
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
    public void getHistory(Map map) {
        Disposable disposable = httpAPIWrapper.getHistoryRecord(map)
                .subscribe(new Consumer<HistoryRecord>() {
                    @Override
                    public void accept(HistoryRecord user) throws Exception {
                        //isSuccesse
                        //mView.closeProgressDialog();
                        mView.setData(user);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        throwable.printStackTrace();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
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