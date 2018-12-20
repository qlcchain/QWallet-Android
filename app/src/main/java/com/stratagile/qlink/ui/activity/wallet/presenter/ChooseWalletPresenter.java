package com.stratagile.qlink.ui.activity.wallet.presenter;
import android.support.annotation.NonNull;
import android.view.View;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.EosAccountInfo;
import com.stratagile.qlink.entity.EosKeyAccount;
import com.stratagile.qlink.ui.activity.wallet.contract.ChooseWalletContract;
import com.stratagile.qlink.ui.activity.wallet.ChooseWalletActivity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.eblock.eos4j.Ecc;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of ChooseWalletActivity
 * @date 2018/11/06 11:21:13
 */
public class ChooseWalletPresenter implements ChooseWalletContract.ChooseWalletContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final ChooseWalletContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private ChooseWalletActivity mActivity;

    @Inject
    public ChooseWalletPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull ChooseWalletContract.View view, ChooseWalletActivity activity) {
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
    public void getEosAccountInfo(EosAccount eosAccount) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("account", eosAccount.getAccountName());
        Disposable disposable = httpAPIWrapper.getEosAccountInfo(infoMap)
                .subscribe(new Consumer<EosAccountInfo>() {
                    @Override
                    public void accept(EosAccountInfo baseBack) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        if (!"".equals(baseBack.getData().getData().getCreate_timestamp())) {
                            reportWalletCreated(eosAccount);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
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

    public void reportWalletCreated(EosAccount eosAccount) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", eosAccount.getAccountName());
        infoMap.put("blockChain", "EOS");
        infoMap.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
        infoMap.put("pubKey", eosAccount.getOwnerPublicKey());
        String str = SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "") + eosAccount.getAccountName();
        infoMap.put("signData", Ecc.sign(eosAccount.getOwnerPrivateKey(), str));
        Disposable disposable = httpAPIWrapper.reportWalletCreate(infoMap)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack baseBack) throws Exception {
                        //isSuccesse
                        eosAccount.setIsCreating(false);
                        AppConfig.getInstance().getDaoSession().getEosAccountDao().update(eosAccount);
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