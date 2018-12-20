package com.stratagile.qlink.ui.activity.wallet.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.constant.MainConstant;
import com.stratagile.qlink.data.NeoNodeRPC;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.ui.activity.eth.EthWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.SelectWalletTypeContract;
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity;
import com.stratagile.qlink.utils.DigestUtils;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.eblock.eos4j.Ecc;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of SelectWalletTypeActivity
 * @date 2018/10/19 10:51:45
 */
public class SelectWalletTypePresenter implements SelectWalletTypeContract.SelectWalletTypeContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final SelectWalletTypeContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private SelectWalletTypeActivity mActivity;

    @Inject
    public SelectWalletTypePresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull SelectWalletTypeContract.View view, SelectWalletTypeActivity activity) {
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
    public void createEthWallet() {
        mView.showProgressDialog();
        Observable.create(new ObservableOnSubscribe<EthWallet>() {
            @Override
            public void subscribe(ObservableEmitter<EthWallet> emitter) throws Exception {
                List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
                for (EthWallet wallet : ethWallets) {
                    if (wallet.getIsCurrent()) {
                        wallet.setIsCurrent(false);
                        AppConfig.getInstance().getDaoSession().getEthWalletDao().update(wallet);
                        break;
                    }
                }
                List<Wallet> wallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
                for (Wallet wallet : wallets) {
                    if (wallet.getIsCurrent()) {
                        wallet.setIsCurrent(false);
                        AppConfig.getInstance().getDaoSession().getWalletDao().update(wallet);
                        break;
                    }
                }
                List<EosAccount> eosAccounts = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
                for (EosAccount wallet : eosAccounts) {
                    if (wallet.getIsCurrent()) {
                        wallet.setIsCurrent(false);
                        AppConfig.getInstance().getDaoSession().getEosAccountDao().update(wallet);
                        break;
                    }
                }
                EthWallet ethWallet = ETHWalletUtils.generateMnemonic();
                AppConfig.getInstance().getDaoSession().getEthWalletDao().insert(ethWallet);
                KLog.i(ethWallet.getMnemonic());
                emitter.onNext(ethWallet);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EthWallet>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EthWallet ethWallet) {
                        mView.createEthWalletSuccess(ethWallet);
                        mView.closeProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void reportWalletCreated(String address, String blockChain, String publicKey, String signData) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", address);
        infoMap.put("blockChain", blockChain);
        infoMap.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
        infoMap.put("pubKey", publicKey);
        if (blockChain.equals("NEO")) {
            NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
            infoMap.put("signData", neoNodeRPC.signStr(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "")));
        } else if (blockChain.equals("ETH")) {
            infoMap.put("signData", signData);
        } else if (blockChain.equals("EOS")) {
            infoMap.put("signData", signData);
        }
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

    @Override
    public void createNeoWallet() {
        mView.showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
                for (EthWallet wallet : ethWallets) {
                    if (wallet.getIsCurrent()) {
                        wallet.setIsCurrent(false);
                        AppConfig.getInstance().getDaoSession().getEthWalletDao().update(wallet);
                        break;
                    }
                }
                List<Wallet> wallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
                for (Wallet wallet : wallets) {
                    if (wallet.getIsCurrent()) {
                        wallet.setIsCurrent(false);
                        AppConfig.getInstance().getDaoSession().getWalletDao().update(wallet);
                        break;
                    }
                }
                List<EosAccount> eosAccounts = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
                for (EosAccount wallet : eosAccounts) {
                    if (wallet.getIsCurrent()) {
                        wallet.setIsCurrent(false);
                        AppConfig.getInstance().getDaoSession().getEosAccountDao().update(wallet);
                        break;
                    }
                }
                Account.INSTANCE.createNewWallet();
                neoutils.Wallet wallet = Account.INSTANCE.getWallet();
                Wallet walletWinq = new Wallet();
                walletWinq.setAddress(wallet.getAddress());
                walletWinq.setWif(wallet.getWIF());
                walletWinq.setPrivateKey(Account.INSTANCE.byteArray2String(wallet.getPrivateKey()).toLowerCase());
                walletWinq.setPublicKey(Account.INSTANCE.byteArray2String(wallet.getPrivateKey()));
                walletWinq.setScriptHash(Account.INSTANCE.byteArray2String(wallet.getHashedSignature()));
                walletWinq.setIsCurrent(true);
                if (wallets.size() < 9) {
                    walletWinq.setName("NEO-Wallet 0" + (wallets.size() + 1));
                } else {
                    walletWinq.setName("NEO-Wallet " + (wallets.size() + 1));
                }
                KLog.i();
                walletWinq.toString();
                AppConfig.getInstance().getDaoSession().getWalletDao().insert(walletWinq);
                mView.createNeoWalletSuccess(walletWinq);
            }
        }).start();
    }
}