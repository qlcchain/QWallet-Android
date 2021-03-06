package com.stratagile.qlink.ui.activity.qlc.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.qlc.contract.ImportQlcPrivateKeyContract;
import com.stratagile.qlink.ui.activity.qlc.ImportQlcPrivateKeyFragment;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: presenter of ImportQlcPrivateKeyFragment
 * @date 2019/05/21 09:46:08
 */
public class ImportQlcPrivateKeyPresenter implements ImportQlcPrivateKeyContract.ImportQlcPrivateKeyContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final ImportQlcPrivateKeyContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private ImportQlcPrivateKeyFragment mFragment;

    @Inject
    public ImportQlcPrivateKeyPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull ImportQlcPrivateKeyContract.View view, ImportQlcPrivateKeyFragment fragment) {
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