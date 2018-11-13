package com.stratagile.qlink.ui.activity.wallet.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.ui.activity.wallet.contract.ImportWalletContract;
import com.stratagile.qlink.ui.activity.wallet.ImportWalletActivity;
import com.stratagile.qlink.utils.SpUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of ImportWalletActivity
 * @date 2018/01/23 14:24:49
 */
public class ImportWalletPresenter implements ImportWalletContract.ImportWalletContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final ImportWalletContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private ImportWalletActivity mActivity;

    @Inject
    public ImportWalletPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull ImportWalletContract.View view, ImportWalletActivity activity) {
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
    public void importWallet(Map map) {
        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.importWallet(map)
                .subscribe(new Consumer<CreateWallet>() {
                    @Override
                    public void accept(CreateWallet wallet) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.onImportWalletSuccess(wallet);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        mView.closeProgressDialog();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
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

    @Override
    public void reportWalletCreated(String address, String blockChain) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", address);
        infoMap.put("blockChain", blockChain);
        infoMap.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
        Disposable disposable = httpAPIWrapper.reportWalletCreate(infoMap)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack baseBack) throws Exception {
                        //isSuccesse
                        mView.closeProgressDialog();
                        mView.reportCreatedWalletSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.closeProgressDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.closeProgressDialog();
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}