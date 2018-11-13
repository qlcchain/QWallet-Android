package com.stratagile.qlink.ui.activity.wallet.presenter;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.google.protobuf.ServiceException;
import com.socks.library.KLog;
import com.stratagile.qlink.ColdWallet;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.EthWalletInfo;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.TokenPrice;
import com.stratagile.qlink.ui.activity.wallet.contract.EthTransferContract;
import com.stratagile.qlink.ui.activity.wallet.EthTransferActivity;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of EthTransferActivity
 * @date 2018/10/31 10:17:17
 */
public class EthTransferPresenter implements EthTransferContract.EthTransferContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final EthTransferContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private EthTransferActivity mActivity;

    @Inject
    public EthTransferPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull EthTransferContract.View view, EthTransferActivity activity) {
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
                        mView.getEthWalletBack(baseBack);

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
    public void getToeknPrice(Map map) {
        Disposable disposable = httpAPIWrapper.getTokenPrice(map)
                .subscribe(new Consumer<TokenPrice>() {
                    @Override
                    public void accept(TokenPrice baseBack) throws Exception {
                        //isSuccesse
                        mView.getTokenPriceBack(baseBack);
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

    private void transferRecord(Map map) {
        Disposable disposable = httpAPIWrapper.reportWalletTransaction(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack baseBack) throws Exception {
                        //isSuccesse
                        mView.sendSuccess("success");
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
                        Map<String, Object> infoMap = new HashMap<>();
                        infoMap.put("addressFrom", tokenInfo.getWalletAddress());
                        infoMap.put("addressTo", toAddress);
                        infoMap.put("blockChain", "ETH");
                        infoMap.put("symbol", "ETH");
                        infoMap.put("amount", amount);
                        infoMap.put("txid", s);
                        transferRecord(infoMap);
                        KLog.i("transaction Hash: " + s);
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
    public void transaction(TokenInfo tokenInfo, String toAddress, String amount, int limit, int price) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(generateTransaction(tokenInfo.getWalletAddress(), tokenInfo.getTokenAddress(), toAddress, derivePrivateKey(tokenInfo.getWalletAddress()), amount, limit, price, tokenInfo.getTokenDecimals()));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        mView.closeProgressDialog();
                        if ("".equals(s)) {
                            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.error2));
                        } else {
                            Map<String, Object> infoMap = new HashMap<>();
                            infoMap.put("addressFrom", tokenInfo.getWalletAddress());
                            infoMap.put("addressTo", toAddress);
                            infoMap.put("blockChain", "ETH");
                            infoMap.put("symbol", tokenInfo.getTokenSymol().toUpperCase());
                            infoMap.put("amount", amount);
                            infoMap.put("txid", s);
                            transferRecord(infoMap);
                        }
                        KLog.i("transaction Hash: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        String data = generateTransaction(tokenInfo.getWalletAddress(), tokenInfo.getTokenAddress(), toAddress, derivePrivateKey(tokenInfo.getWalletAddress()), amount, limit, price);
//        KLog.i(data);
    }

    private String generateTransaction(String fromAddress, String contractAddress, String toAddress, String privateKey, String amount, int limit, int price, int decimals) {
        final Web3j web3j = Web3jFactory.build(new HttpService("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk"));
        try {
            return testTokenTransaction(web3j, fromAddress, privateKey, contractAddress, toAddress, amount, decimals, limit, price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String testTokenTransaction(Web3j web3j, String fromAddress, String privateKey, String contractAddress, String toAddress, String amount, int decimals, int limit, int price) {
        BigInteger nonce;
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ethGetTransactionCount == null) {
            return "";
        }
        nonce = ethGetTransactionCount.getTransactionCount();
        System.out.println("nonce " + nonce);
        BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(price), Convert.Unit.GWEI).toBigInteger();
        BigInteger gasLimit = BigInteger.valueOf(limit);
        BigInteger value = BigInteger.ZERO;

        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(toAddress), new Uint256(baseToSubunit(amount, decimals))),
                Arrays.asList(new TypeReference<Type>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);


        KLog.i(encodedFunction);
        byte chainId = ChainId.MAINNET;
        String signedData;
        try {
            signedData = ColdWallet.signTransaction(nonce, gasPrice, gasLimit, contractAddress, value, encodedFunction, chainId, privateKey);
            if (signedData != null) {
                KLog.i(signedData);
                //如果客户端发送的话，就把下面三行打开
                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
                if (ethSendTransaction.hasError()) {
                    KLog.i(ethSendTransaction.getError().getMessage());
                } else {
                }
                System.out.println("交易的hash为：" + ethSendTransaction.getTransactionHash());
                return ethSendTransaction.getTransactionHash();
//                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
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

    /**
     * Base - taken to mean default unit for a currency e.g. ETH, DOLLARS
     * Subunit - taken to mean subdivision of base e.g. WEI, CENTS
     *
     * @param baseAmountStr - decimal amonut in base unit of a given currency
     * @param decimals - decimal places used to convert to subunits
     * @return amount in subunits
     */
    public static BigInteger baseToSubunit(String baseAmountStr, int decimals) {
        assert(decimals >= 0);
        BigDecimal baseAmount = new BigDecimal(baseAmountStr);
        BigDecimal subunitAmount = baseAmount.multiply(BigDecimal.valueOf(10).pow(decimals));
        try {
            return subunitAmount.toBigIntegerExact();
        } catch (ArithmeticException ex) {
            assert(false);
            return subunitAmount.toBigInteger();
        }
    }

}