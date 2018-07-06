package com.stratagile.qlink.ui.activity.wordcup.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.wordcup.contract.OpenMainWalletContract;
import com.stratagile.qlink.ui.activity.wordcup.OpenMainWalletFragment;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: presenter of OpenMainWalletFragment
 * @date 2018/06/13 17:37:05
 */
public class OpenMainWalletPresenter implements OpenMainWalletContract.OpenMainWalletContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final OpenMainWalletContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private OpenMainWalletFragment mFragment;

    @Inject
    public OpenMainWalletPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull OpenMainWalletContract.View view, OpenMainWalletFragment fragment) {
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
//                      //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
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