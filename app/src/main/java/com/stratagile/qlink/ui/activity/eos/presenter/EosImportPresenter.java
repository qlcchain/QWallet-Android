package com.stratagile.qlink.ui.activity.eos.presenter;

import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.blockchain.cypto.util.HexUtils;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.EosAccountInfo;
import com.stratagile.qlink.entity.EosKeyAccount;
import com.stratagile.qlink.ui.activity.eos.contract.EosImportContract;
import com.stratagile.qlink.ui.activity.eos.EosImportActivity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.eblock.eos4j.Ecc;
import io.eblock.eos4j.utils.Hex;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: presenter of EosImportActivity
 * @date 2018/11/26 17:06:38
 */
public class EosImportPresenter implements EosImportContract.EosImportContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final EosImportContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private EosImportActivity mActivity;

    @Inject
    public EosImportPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull EosImportContract.View view, EosImportActivity activity) {
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

    public void getEosKeyAccount(Map map) {
        Disposable disposable = httpAPIWrapper.getKeyAccount(map)
                .subscribe(new Consumer<EosKeyAccount>() {
                    @Override
                    public void accept(EosKeyAccount baseBack) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.getEosKeyAccountBack(baseBack);
                        mView.closeProgressDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        ToastUtil.displayShortToast(throwable.getMessage());
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

    @Override
    public void getEosAccountInfo(Map map) {
        Disposable disposable = httpAPIWrapper.getEosAccountInfo(map)
                .subscribe(new Consumer<EosAccountInfo>() {
                    @Override
                    public void accept(EosAccountInfo baseBack) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.accountInfoBack(baseBack);
                        mView.closeProgressDialog();
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

    @Override
    public void reportWalletCreated(String address, String publicKey, String privateKey) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", address);
        infoMap.put("blockChain", "EOS");
        infoMap.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
        infoMap.put("pubKey", publicKey);
        String str = SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "") + address;
        infoMap.put("signData", Ecc.sign(privateKey, str));
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