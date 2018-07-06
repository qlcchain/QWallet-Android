package com.stratagile.qlink.ui.activity.wallet.presenter;
import android.support.annotation.NonNull;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletListContract;
import com.stratagile.qlink.ui.activity.wallet.WalletListFragment;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of WalletListFragment
 * @date 2018/01/09 15:08:22
 */
public class WalletListPresenter implements WalletListContract.WalletListContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final WalletListContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private WalletListFragment mFragment;

    @Inject
    public WalletListPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull WalletListContract.View view, WalletListFragment fragment) {
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
    public void getWalletListFromServer(String walletStatus, int requestPage, int onePageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", onePageSize + "");
        map.put("pageNum", requestPage + "");
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