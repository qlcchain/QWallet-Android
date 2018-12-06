package com.stratagile.qlink.ui.activity.eos.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.EosAccountInfo;
import com.stratagile.qlink.entity.EosResource;
import com.stratagile.qlink.entity.EosTokens;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.ui.activity.eos.contract.EosResourceManagementContract;
import com.stratagile.qlink.ui.activity.eos.EosResourceManagementActivity;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: presenter of EosResourceManagementActivity
 * @date 2018/12/04 18:08:32
 */
public class EosResourceManagementPresenter implements EosResourceManagementContract.EosResourceManagementContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final EosResourceManagementContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private EosResourceManagementActivity mActivity;

    @Inject
    public EosResourceManagementPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull EosResourceManagementContract.View view, EosResourceManagementActivity activity) {
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
    public void getEosResource(Map map) {
        Disposable disposable = httpAPIWrapper.getEosTResource(map)
                .subscribe(new Consumer<EosResource>() {
                    @Override
                    public void accept(EosResource baseBack) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.setEosResource(baseBack);
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
    public void getEosWalletDetail(String address, Map map) {
        Disposable disposable = httpAPIWrapper.getEosTokenList(map)
                .subscribe(new Consumer<EosTokens>() {
                    @Override
                    public void accept(EosTokens baseBack) throws Exception {
                        //isSuccesse
                        getEosTokensInfo(address, baseBack);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void getEosTokensInfo(String accountName, EosTokens eosTokens) {
        ArrayList<TokenInfo> tokenInfos = new ArrayList<>();
        for (int i = 0; i < eosTokens.getData().getData().getSymbol_list().size(); i++) {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setTokenName(eosTokens.getData().getData().getSymbol_list().get(i).getSymbol());
            tokenInfo.setTokenSymol(eosTokens.getData().getData().getSymbol_list().get(i).getSymbol());
            tokenInfo.setTokenValue(Double.parseDouble(eosTokens.getData().getData().getSymbol_list().get(i).getBalance()));
            tokenInfo.setTokenAddress(eosTokens.getData().getData().getSymbol_list().get(i).getCodeX());
            tokenInfo.setWalletAddress(accountName);
            tokenInfo.setMainNetToken(true);
            tokenInfo.setWalletType(AllWallet.WalletType.EosWallet);
            tokenInfos.add(tokenInfo);
        }
        mView.getTokenPriceBack(tokenInfos);
    }
}