package com.stratagile.qlink.ui.activity.mainwallet.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.MainHttpAPIWrapper;
import com.stratagile.qlink.entity.Record;
import com.stratagile.qlink.ui.activity.mainwallet.contract.MainTransactionRecordListContract;
import com.stratagile.qlink.ui.activity.mainwallet.MainTransactionRecordListFragment;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: presenter of MainTransactionRecordListFragment
 * @date 2018/06/13 20:52:12
 */
public class MainTransactionRecordListPresenter implements MainTransactionRecordListContract.MainTransactionRecordListContractPresenter{

    MainHttpAPIWrapper httpAPIWrapper;
    private final MainTransactionRecordListContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private MainTransactionRecordListFragment mFragment;

    @Inject
    public MainTransactionRecordListPresenter(@NonNull MainHttpAPIWrapper httpAPIWrapper, @NonNull MainTransactionRecordListContract.View view, MainTransactionRecordListFragment fragment) {
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
    public void getMainTransactionRecordListFromServer(String mainTransactionRecordStatus, int requestPage, int onePageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", onePageSize + "");
        map.put("pageNum", requestPage + "");
    }

    @Override
    public void getRecords(Map map) {
        Disposable disposable = httpAPIWrapper.recordQuerys(map)
                .subscribe(new Consumer<Record>() {
                    @Override
                    public void accept(Record record) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        //mView.closeProgressDialog();
                        mView.getRecordSuccess(record);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
                        //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        mView.closeProgressDialog();
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}