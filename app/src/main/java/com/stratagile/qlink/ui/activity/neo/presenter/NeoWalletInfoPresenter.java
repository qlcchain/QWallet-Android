package com.stratagile.qlink.ui.activity.neo.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.neo.contract.NeoWalletInfoContract;
import com.stratagile.qlink.ui.activity.neo.NeoWalletInfoActivity;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of NeoWalletInfoActivity
 * @date 2018/11/05 17:19:51
 */
public class NeoWalletInfoPresenter implements NeoWalletInfoContract.NeoWalletInfoContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final NeoWalletInfoContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private NeoWalletInfoActivity mActivity;

    @Inject
    public NeoWalletInfoPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull NeoWalletInfoContract.View view, NeoWalletInfoActivity activity) {
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