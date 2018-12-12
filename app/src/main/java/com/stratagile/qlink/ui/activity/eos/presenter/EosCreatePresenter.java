package com.stratagile.qlink.ui.activity.eos.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.ColdWallet;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.EosAccountInfo;
import com.stratagile.qlink.entity.EosKeyAccount;
import com.stratagile.qlink.entity.EthWalletInfo;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.eos.EosNeedInfo;
import com.stratagile.qlink.ui.activity.eos.contract.EosCreateContract;
import com.stratagile.qlink.ui.activity.eos.EosCreateActivity;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

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
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: presenter of EosCreateActivity
 * @date 2018/12/07 11:29:04
 */
public class EosCreatePresenter implements EosCreateContract.EosCreateContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final EosCreateContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private EosCreateActivity mActivity;

    @Inject
    public EosCreatePresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull EosCreateContract.View view, EosCreateActivity activity) {
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

    private void getEthTokensInfo(EthWalletInfo ethWalletInfo) {
        TokenInfo tokenInfo1 = new TokenInfo();
        tokenInfo1.setTokenName("ETH");
        tokenInfo1.setTokenSymol("ETH");
        tokenInfo1.setTokenAddress(ethWalletInfo.getData().getAddress());
        if ("false".equals(ethWalletInfo.getData().getETH().getBalance().toString()) || "-1.0".equals(ethWalletInfo.getData().getETH().getBalance().toString())) {
            tokenInfo1.setTokenValue(0);
        } else {
            tokenInfo1.setTokenValue(Double.parseDouble(ethWalletInfo.getData().getETH().getBalance().toString()));
        }
        tokenInfo1.setTokenImgName("eth_eth");
        tokenInfo1.setWalletType(AllWallet.WalletType.EthWallet);
        tokenInfo1.setWalletAddress(ethWalletInfo.getData().getAddress());
        tokenInfo1.setMainNetToken(true);
        mView.setEth(tokenInfo1);
    }

    @Override
    public void transactionEth(TokenInfo tokenInfo, String toAddress, String amount, int limit, int price) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(generateTransactionEth(tokenInfo.getWalletAddress(), toAddress, derivePrivateKey(tokenInfo.getWalletAddress()), amount, limit, price));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        KLog.i("transaction Hash: " + s);
                        mView.transferEthSuccess(s);
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
    public void getEosAccountInfo(Map map, int flag) {
        Disposable disposable = httpAPIWrapper.getEosAccountInfo(map)
                .subscribe(new Consumer<EosAccountInfo>() {
                    @Override
                    public void accept(EosAccountInfo baseBack) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.accountInfoBack(baseBack, flag);
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
    public void getEosNeedInfo(Map map) {
        Disposable disposable = httpAPIWrapper.getEosNeedInfo(map)
                .subscribe(new Consumer<EosNeedInfo>() {
                    @Override
                    public void accept(EosNeedInfo baseBack) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.setEosNeedInfo(baseBack);
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
    public void getEosKeyAccount(Map map) {
        Disposable disposable = httpAPIWrapper.getKeyAccount(map)
                .subscribe(new Consumer<ArrayList<EosKeyAccount>>() {
                    @Override
                    public void accept(ArrayList<EosKeyAccount> baseBack) throws Exception {
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
    public void createEosAccount(Map map) {
        Disposable disposable = httpAPIWrapper.createEosAccount(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack baseBack) throws Exception {
                        //isSuccesse
                        mView.createEosAccountSuccess(baseBack.getMsg());
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

    private String generateTransactionEth(String fromAddress, String toAddress, String privateKey, String amount, int limit, int price) {
        final Web3j web3j = Web3jFactory.build(new HttpService("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk"));
        try {
            return testEthTransaction(web3j, fromAddress, privateKey, toAddress, amount, limit, price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String testEthTransaction(Web3j web3j, String fromAddress, String privateKey, String toAddress, String amount, int limit, int price) {

        BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(price), Convert.Unit.GWEI).toBigInteger();
        BigInteger gasLimit = BigInteger.valueOf(limit);
        BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        KLog.i(value);
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        String signedData = ColdWallet.signTransactionEth(nonce, toAddress, gasPrice, gasLimit, value, privateKey);

        EthSendTransaction ethSendTransaction = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(signedData).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (ethSendTransaction.hasError()) {
            KLog.i(ethSendTransaction.getError().getMessage());
        } else {
            String transactionHash = ethSendTransaction.getTransactionHash();
            KLog.i(transactionHash);
            return transactionHash;
        }
        return "";
    }

    private String derivePrivateKey(String address) {
        List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        EthWallet ethWallet = new EthWallet();
        for (int i = 0; i < ethWallets.size(); i++) {
            if (ethWallets.get(i).getAddress().toLowerCase().equals(address)) {
                ethWallet = ethWallets.get(i);
                break;
            }
        }
        return ETHWalletUtils.derivePrivateKey(ethWallet.getId());
    }


}