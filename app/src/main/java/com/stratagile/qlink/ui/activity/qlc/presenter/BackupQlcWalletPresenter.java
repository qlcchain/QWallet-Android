package com.stratagile.qlink.ui.activity.qlc.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.qlc.contract.BackupQlcWalletContract;
import com.stratagile.qlink.ui.activity.qlc.BackupQlcWalletActivity;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of BackupQlcWalletActivity
 * @date 2019/05/20 10:52:55
 */
public class BackupQlcWalletPresenter implements BackupQlcWalletContract.BackupQlcWalletContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final BackupQlcWalletContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private BackupQlcWalletActivity mActivity;

    @Inject
    public BackupQlcWalletPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull BackupQlcWalletContract.View view, BackupQlcWalletActivity activity) {
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