package com.stratagile.qlink.ui.activity.eth.presenter;

import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.ColdWallet;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.entity.BuyQlc;
import com.stratagile.qlink.ui.activity.eth.contract.BnbToQlcContract;
import com.stratagile.qlink.ui.activity.eth.BnbToQlcActivity;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.WalletStorage;

import org.json.JSONException;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: presenter of BnbToQlcActivity
 * @date 2018/05/24 10:46:37
 */
public class BnbToQlcPresenter implements BnbToQlcContract.BnbToQlcContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final BnbToQlcContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private BnbToQlcActivity mActivity;

    @Inject
    public BnbToQlcPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull BnbToQlcContract.View view, BnbToQlcActivity activity) {
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
    public void parseWallet(String password, String address, String amount) {
        mView.showProgressDialog();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext(derivePrivateKey(password, address));
//                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String privateKey) {
                        mView.closeProgressDialog();
                        if ("".equals(privateKey)) {
                            return;
                        }
                        mView.showProgressDialog();
                        Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                emitter.onNext(generateTransaction(address, privateKey, amount));
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
                                        KLog.i("transaction Hash: " + s);
                                        mView.startTransaction(s);
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
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void startBnb2Qlc(Map map) {
        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.bnb2qlc(map)
                .subscribe(new Consumer<BuyQlc>() {
                    @Override
                    public void accept(BuyQlc buyQlc) throws Exception {
                        KLog.i("onSuccesse");
                        if (buyQlc.getData().isResult()) {
                            mView.transactionSuccess(buyQlc.getData().getRecordId());
                            TransactionRecord transactionRecord = new TransactionRecord();
                            transactionRecord.setExChangeId(buyQlc.getData().getRecordId());
                            transactionRecord.setTxid(buyQlc.getData().getRecordId());
                            transactionRecord.setTimestamp(Calendar.getInstance().getTimeInMillis());
                            transactionRecord.setTransactiomType(TransactionRecord.transactionType.transactionBnb2Qlc.ordinal());
                            AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(transactionRecord);
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

    private String generateTransaction(String address, String privateKey, String amount) {
        final Web3j web3j = Web3jFactory.build(new HttpService("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk"));
        try {
//			createWallet("11111111");
//			decryptWallet(keystore, "11111111");
//			testTransaction();
            return testTokenTransaction(web3j, address, privateKey, ConstantValue.ethContractAddress, ConstantValue.ethMainAddress, Double.parseDouble(amount), 18);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String derivePrivateKey(String password, String address) {
        Credentials credentials = null;
        try {
            credentials = WalletStorage.getInstance(AppConfig.getInstance()).getFullWallet(AppConfig.getInstance(), password, address);
            String privateKey = Numeric.toHexStringNoPrefixZeroPadded(credentials.getEcKeyPair().getPrivateKey(), Keys.PRIVATE_KEY_LENGTH_IN_HEX);
            KLog.i(privateKey);
            KLog.i(address);
            return privateKey;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.Invalid_password_provided));
        }
        return "";
    }

    private String testTokenTransaction(Web3j web3j, String fromAddress, String privateKey, String contractAddress, String toAddress, double amount, int decimals) {
        BigInteger nonce;
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ethGetTransactionCount == null) {
            return "";
        }
        nonce = ethGetTransactionCount.getTransactionCount();
        System.out.println("nonce " + nonce);
        BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(15), Convert.Unit.GWEI).toBigInteger();
        BigInteger gasLimit = BigInteger.valueOf(60000);
        BigInteger value = BigInteger.ZERO;
        //token转账参数
        String methodName = "transfer";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        org.web3j.abi.datatypes.Address tAddress = new org.web3j.abi.datatypes.Address(toAddress);
        Uint256 tokenValue = new Uint256(BigDecimal.valueOf(amount).multiply(BigDecimal.TEN.pow(decimals)).toBigInteger());
        inputParameters.add(tAddress);
        inputParameters.add(tokenValue);
        TypeReference<Bool> typeReference = new TypeReference<Bool>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        KLog.i(data);
        byte chainId = ChainId.NONE;
        String signedData;
        try {
            signedData = ColdWallet.signTransaction(nonce, gasPrice, gasLimit, contractAddress, value, data, chainId, privateKey);
            if (signedData != null) {
                KLog.i(signedData);
                //如果客户端发送的话，就把下面三行打开
//                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
//                System.out.println(ethSendTransaction.getTransactionHash());
//                return ethSendTransaction.getTransactionHash();
                return signedData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}