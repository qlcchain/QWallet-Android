package com.stratagile.qlink.ui.activity.neo.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.neo.contract.WalletCreatedContract;
import com.stratagile.qlink.ui.activity.neo.WalletCreatedActivity;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of WalletCreatedActivity
 * @date 2018/01/24 16:27:17
 */
public class WalletCreatedPresenter implements WalletCreatedContract.WalletCreatedContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final WalletCreatedContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private WalletCreatedActivity mActivity;

    @Inject
    public WalletCreatedPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull WalletCreatedContract.View view, WalletCreatedActivity activity) {
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