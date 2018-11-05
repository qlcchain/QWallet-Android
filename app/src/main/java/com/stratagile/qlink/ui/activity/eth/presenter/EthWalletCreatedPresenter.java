package com.stratagile.qlink.ui.activity.eth.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.eth.contract.EthWalletCreatedContract;
import com.stratagile.qlink.ui.activity.eth.EthWalletCreatedActivity;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: presenter of EthWalletCreatedActivity
 * @date 2018/10/23 15:15:28
 */
public class EthWalletCreatedPresenter implements EthWalletCreatedContract.EthWalletCreatedContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final EthWalletCreatedContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private EthWalletCreatedActivity mActivity;

    @Inject
    public EthWalletCreatedPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull EthWalletCreatedContract.View view, EthWalletCreatedActivity activity) {
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