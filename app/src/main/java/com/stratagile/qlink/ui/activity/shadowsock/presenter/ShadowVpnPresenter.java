package com.stratagile.qlink.ui.activity.shadowsock.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.shadowsock.ShadowVpnActivity;
import com.stratagile.qlink.ui.activity.shadowsock.contract.ShadowVpnContract;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.shadowsock
 * @Description: presenter of ShadowVpnActivity
 * @date 2018/08/07 11:54:13
 */
public class ShadowVpnPresenter implements ShadowVpnContract.ShadowVpnContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final ShadowVpnContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private ShadowVpnActivity mActivity;

    @Inject
    public ShadowVpnPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull ShadowVpnContract.View view, ShadowVpnActivity activity) {
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