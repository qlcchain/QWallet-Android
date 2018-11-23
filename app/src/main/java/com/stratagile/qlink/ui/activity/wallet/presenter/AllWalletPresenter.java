package com.stratagile.qlink.ui.activity.wallet.presenter;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.api.HttpObserver;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.ClaimData;
import com.stratagile.qlink.entity.EthWalletInfo;
import com.stratagile.qlink.entity.GotWinqGas;
import com.stratagile.qlink.entity.NeoTransfer;
import com.stratagile.qlink.entity.NeoWalletInfo;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.TokenPrice;
import com.stratagile.qlink.entity.WinqGasBack;
import com.stratagile.qlink.ui.activity.wallet.contract.AllWalletContract;
import com.stratagile.qlink.ui.activity.wallet.AllWalletFragment;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of AllWalletFragment
 * @date 2018/10/24 10:17:57
 */
public class AllWalletPresenter implements AllWalletContract.AllWalletContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final AllWalletContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private AllWalletFragment mFragment;

    @Inject
    public AllWalletPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull AllWalletContract.View view, AllWalletFragment fragment) {
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
    public void getETHWalletDetail(String address, Map map) {
        Disposable disposable = httpAPIWrapper.getEthWalletInfo(map)
                .subscribe(new Consumer<EthWalletInfo>() {
                    @Override
                    public void accept(EthWalletInfo baseBack) throws Exception {
                        //isSuccesse
                        getEthTokensInfo(baseBack);
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

    @Override
    public void getNeoWalletDetail(String address, Map map) {
        Disposable disposable = httpAPIWrapper.getNeoWalletInfo(map)
                .subscribe(new Consumer<NeoWalletInfo>() {
                    @Override
                    public void accept(NeoWalletInfo baseBack) throws Exception {
                        //isSuccesse
                        getNeoTokensInfo(baseBack);
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

    @Override
    public void getToeknPrice(ArrayList<TokenInfo> arrayList, HashMap map) {
        Disposable disposable = httpAPIWrapper.getTokenPrice(map)
                .subscribe(new Consumer<TokenPrice>() {
                    @Override
                    public void accept(TokenPrice baseBack) throws Exception {
                        //isSuccesse
                        for (int i = 0; i < baseBack.getData().size(); i++) {
                            if (arrayList.get(i).isMainNetToken()) {
                                arrayList.get(i).setTokenPrice(baseBack.getData().get(i).getPrice());
                            }
                        }
                        mView.getTokenPriceBack(arrayList);
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

    @Override
    public void getWinqGas(String address) {
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        httpAPIWrapper.getBalance(map)
                .subscribe(new HttpObserver<Balance>() {
                    @Override
                    public void onNext(Balance balance) {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        balance.setWalletAddress(address);
                        mView.getWinqGasBack(balance);
                    }
                });
    }

    @Override
    public void queryWinqGas(Map map) {
        Disposable disposable =  httpAPIWrapper.queryWinqGas(map)
                .subscribe(new Consumer<WinqGasBack>() {
                    @Override
                    public void accept(WinqGasBack winqGasBack) throws Exception {
                        mView.queryWinqGasBack(winqGasBack);
                        mView.closeProgressDialog();
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
    public void gotWinqGas(Map map) {
        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.gotWinqGas(map)
                .subscribe(new Consumer<GotWinqGas>() {
                    @Override
                    public void accept(GotWinqGas baseBack) throws Exception {
                        //isSuccesse
                        mView.gotWinqGasSuccess("Claimed Successfully");
                        mView.closeProgressDialog();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.gotWinqGasSuccess("Network Error. Please try later.");
                        mView.closeProgressDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.gotWinqGasSuccess("Network Error. Please try later.");
                        mView.closeProgressDialog();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getNeoGasClaim(Map map) {
        Disposable disposable = httpAPIWrapper.neoGasClaim(map)
                .subscribe(new Consumer<ClaimData>() {
                    @Override
                    public void accept(ClaimData baseBack) throws Exception {
                        //isSuccesse
                        mView.queryGasClaimBack(baseBack);
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


    private void getTokenPrice(ArrayList<TokenInfo> tokenInfos) {
        if (tokenInfos == null || tokenInfos.size() == 0) {
            mView.getTokenPriceBack(new ArrayList<>());
            return;
        }
        HashMap<String, Object> infoMap = new HashMap<>();
        String[] tokens = new String[tokenInfos.size()];
        for (int i = 0; i < tokenInfos.size(); i++) {
            tokens[i] = tokenInfos.get(i).getTokenSymol();
        }
        infoMap.put("symbols", tokens);
        infoMap.put("coin", ConstantValue.currencyBean.getName());

        getToeknPrice(tokenInfos, infoMap);
    }

    private void getEthTokensInfo(EthWalletInfo ethWalletInfo) {
        ArrayList<TokenInfo> tokenInfos = new ArrayList<>();
        TokenInfo tokenInfo1 = new TokenInfo();
        tokenInfo1.setTokenName("ETH");
        tokenInfo1.setTokenSymol("ETH");
        tokenInfo1.setTokenAddress(ethWalletInfo.getData().getAddress());
        tokenInfo1.setTokenValue(ethWalletInfo.getData().getETH().getBalance());
        tokenInfo1.setTokenImgName("eth_eth");
        tokenInfo1.setWalletType(AllWallet.WalletType.EthWallet);
        tokenInfo1.setWalletAddress(ethWalletInfo.getData().getAddress());
        tokenInfo1.setMainNetToken(true);
        tokenInfos.add(tokenInfo1);
        if (ethWalletInfo.getData().getTokens() == null) {
            getTokenPrice(tokenInfos);
            return;
        }
        for (int i = 0; i < ethWalletInfo.getData().getTokens().size(); i++) {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setTokenName(ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getSymbol());
            tokenInfo.setTokenSymol(ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getSymbol());
            tokenInfo.setTokenValue(ethWalletInfo.getData().getTokens().get(i).getBalance());
            tokenInfo.setTokenAddress(ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getAddress());
            tokenInfo.setTokenDecimals(new Integer(ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getDecimals()));
            tokenInfo.setWalletType(AllWallet.WalletType.EthWallet);
            tokenInfo.setWalletAddress(ethWalletInfo.getData().getAddress());
            if (isEthMainNetToken(ethWalletInfo.getData().getTokens().get(i))) {
                tokenInfo.setTokenImgName(getEthTokenImg(ethWalletInfo.getData().getTokens().get(i)));
                tokenInfo.setMainNetToken(true);
            } else {
                tokenInfo.setMainNetToken(false);
            }
            tokenInfos.add(tokenInfo);
        }
        getTokenPrice(tokenInfos);
    }

    private int getTokenCount(EthWalletInfo.DataBean.TokensBean tokensBean) {
        return (int) (tokensBean.getBalance() / Math.pow(10, Double.parseDouble(tokensBean.getTokenInfo().getDecimals())));
    }

    /**
     * 判断该token是否为主网的token，是否有估值
     *
     * @return
     */
    private boolean isEthMainNetToken(EthWalletInfo.DataBean.TokensBean tokensBean) {
        String jsonStr = new Gson().toJson(tokensBean.getTokenInfo().getPrice());
        KLog.i(jsonStr);
        if (!"false".equals(jsonStr)) {
            return true;
        } else {
            //没有上交易所
            return false;
        }
    }

    private String getEthTokenImg(EthWalletInfo.DataBean.TokensBean tokensBean) {
        String jsonStr = new Gson().toJson(tokensBean.getTokenInfo().getPrice());
        KLog.i(jsonStr);
        if (!"false".equals(jsonStr)) {
            JSONObject.parseObject(jsonStr).getString("ts");
            return "eth_" + tokensBean.getTokenInfo().getSymbol().toLowerCase();
        } else {
            //没有上交易所
            return "";
        }
    }

    private void getNeoTokensInfo(NeoWalletInfo neoWalletInfo) {
        ArrayList<TokenInfo> tokenInfos = new ArrayList<>();
        if (neoWalletInfo.getData().getBalance().size() == 0) {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setTokenName("NEO");
            tokenInfo.setTokenSymol("NEO");
            tokenInfo.setTokenValue(0);
            tokenInfo.setTokenAddress("");
            tokenInfo.setTokenImgName("neo_neo");
            tokenInfo.setWalletAddress(neoWalletInfo.getData().getAddress());
            tokenInfo.setMainNetToken(true);
            tokenInfo.setWalletType(AllWallet.WalletType.NeoWallet);
            tokenInfos.add(tokenInfo);
        }
        for (int i = 0; i < neoWalletInfo.getData().getBalance().size(); i++) {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setTokenName(neoWalletInfo.getData().getBalance().get(i).getAsset());
            tokenInfo.setTokenSymol(neoWalletInfo.getData().getBalance().get(i).getAsset_symbol());
            tokenInfo.setTokenValue(neoWalletInfo.getData().getBalance().get(i).getAmount());
            tokenInfo.setTokenAddress(neoWalletInfo.getData().getBalance().get(i).getAsset_hash());
            tokenInfo.setTokenImgName(getNeoTokenImg(neoWalletInfo.getData().getBalance().get(i)));
            tokenInfo.setWalletAddress(neoWalletInfo.getData().getAddress());
            tokenInfo.setMainNetToken(true);
            tokenInfo.setWalletType(AllWallet.WalletType.NeoWallet);
            tokenInfos.add(tokenInfo);
        }
        getTokenPrice(tokenInfos);
    }

    private String getNeoTokenImg(NeoWalletInfo.DataBean.BalanceBean balanceBean) {
        return "neo_" + balanceBean.getAsset_symbol().toLowerCase();
    }

    @Override
    public void claimGas(String address, String amount, String txid) {
        mView.showProgressDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("addressFrom", address);
        map.put("addressTo", address);
        map.put("symbol", "GAS");
        map.put("amount", amount);
        map.put("tx", txid);
        Disposable disposable = httpAPIWrapper.neoTokenTransaction(map)
                .subscribe(new Consumer<NeoTransfer>() {
                    @Override
                    public void accept(NeoTransfer baseBack) throws Exception {
                        //isSuccesse
                        mView.claimGasBack(baseBack);
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