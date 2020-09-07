package com.stratagile.qlink.ui.activity.main.presenter;
import android.support.annotation.NonNull;

import com.stratagile.qlink.Account;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.ui.activity.main.contract.GuestContract;
import com.stratagile.qlink.ui.activity.main.GuestActivity;
import com.stratagile.qlink.utils.WalletKtutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: presenter of GuestActivity
 * @date 2018/06/21 15:39:34
 */
public class GuestPresenter implements GuestContract.GuestContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final GuestContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private GuestActivity mActivity;

    @Inject
    public GuestPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull GuestContract.View view, GuestActivity activity) {
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
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String[] privateKey = map.get("key").toString().split(",");
//                List<String> privateKeyList = Arrays.asList(privateKey);
//                ArrayList<Wallet> walletArrayList = new ArrayList<>();
//                for (int i = 0; i < privateKeyList.size(); i++) {
//                    if (Account.INSTANCE.fromHex(privateKeyList.get(i))) {
//                        neoutils.Wallet wallet = Account.INSTANCE.getWallet();
//                        com.stratagile.qlink.db.Wallet wallet1 = new com.stratagile.qlink.db.Wallet();
//                        wallet1.setIsMain(false);
//                        wallet1.setAddress(wallet.getAddress());
//                        wallet1.setPrivateKey(WalletKtutil.byteArrayToHex(wallet.getPrivateKey()));
//                        wallet1.setPublicKey(WalletKtutil.byteArrayToHex(wallet.getPublicKey()));
//                        wallet1.setScriptHash(WalletKtutil.byteArrayToHex(wallet.getHashedSignature()));
//                        wallet1.setWif(wallet.getWIF());
//                        walletArrayList.add(wallet1);
//                        AppConfig.getInstance().getDaoSession().getWalletDao().insert(wallet1);
//                        ConstantValue.canClickWallet = true;
//                    }
//                }
            }
        }).start();
    }

}