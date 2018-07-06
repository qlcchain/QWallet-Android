package com.stratagile.qlink.ui.activity.wallet.presenter;

import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.api.HttpObserver;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.Raw;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletContract;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of WalletFragment
 * @date 2018/01/18 19:08:00
 */
public class WalletPresenter implements WalletContract.WalletContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final WalletContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public WalletPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull WalletContract.View view) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
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
    public void createWallet(Map map) {
        httpAPIWrapper.createWallet(map).subscribe(new HttpObserver<BaseBack>() {
            Disposable disposable;
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }
            @Override
            public void onNext(BaseBack baseBack) {
                onComplete();
                disposable.dispose();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
            @Override
            public void onComplete() {
                KLog.i("onComplete, 解除订阅");
                disposable.dispose();
            }

        });
//        Disposable disposable = httpAPIWrapper.createWallet(map)
//                .subscribe(new Consumer<BaseBack>() {
//                    @Override
//                    public void accept(BaseBack wallet) throws Exception {
//                        //isSuccesse
//                        KLog.i("onSuccesse");
////                        mView.onCreatWalletSuccess(wallet.getData());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        //onError
//                        KLog.i("onError");
//                        throwable.printStackTrace();
//                        //mView.closeProgressDialog();
//                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        //onComplete
//                        KLog.i("onComplete");
//                    }
//                });
//        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getBalance(Map map) {
        httpAPIWrapper.getBalance(map).subscribe(new HttpObserver<Balance>() {
            @Override
            public void onNext(Balance balance) {
                KLog.i("onSuccesse");
                mView.onGetBalancelSuccess(balance);
                onComplete();
            }
            Disposable disposable;
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.onGetRawError();
            }
            @Override
            public void onComplete() {
                KLog.i("onComplete, 解除订阅");
                disposable.dispose();
                mView.onGetRawError();
            }
        });
//        Disposable disposable = httpAPIWrapper.getBalance(map)
//                .subscribe(new Consumer<Balance>() {
//                    @Override
//                    public void accept(Balance balance) throws Exception {
//                        //isSuccesse
//                        KLog.i("onSuccesse");
//                        mView.onGetBalancelSuccess(balance);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        //onError
//                        KLog.i("onError");
//                        throwable.printStackTrace();
//                        //mView.closeProgressDialog();
//                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        //onComplete
//                        KLog.i("onComplete");
//                    }
//                });
//        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getRaw(Map map) {
        httpAPIWrapper.getRaw(map).subscribe(new HttpObserver<Raw>() {
            Disposable disposable;
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }
            @Override
            public void onNext(Raw raw) {
                mView.onGetRawSuccess(raw);
                onComplete();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.onGetRawError();
            }
            @Override
            public void onComplete() {
                KLog.i("onComplete, 解除订阅");
                disposable.dispose();
                mView.onGetRawError();
            }
        });
//        Disposable disposable = httpAPIWrapper.getRaw(map)
//                .subscribe(new Consumer<Raw>() {
//                    @Override
//                    public void accept(Raw raw) throws Exception {
//                        //isSuccesse
//                        KLog.i("onSuccesse");
//                        mView.onGetRawSuccess(raw);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        //onError
//                        KLog.i("onError");
//                        throwable.printStackTrace();
//                        //mView.closeProgressDialog();
//                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        //onComplete
//                        KLog.i("onComplete");
//                    }
//                });
//        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getETHWalletDetail(String address, Map map) {
        final Web3j web3j = Web3jFactory.build(new HttpService("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk"));
        Observable<BigInteger> bnbValue = getTokenBalance(web3j, address, ConstantValue.ethContractAddress);
        bnbValue.observeOn(AndroidSchedulers.mainThread())
                .map(new io.reactivex.functions.Function<BigInteger, String>() {
                    @Override
                    public String apply(BigInteger bigInteger) throws Exception {
                        BigDecimal b = new BigDecimal(new Double(bigInteger.floatValue() / ConstantValue.bnbDecimal).toString());
                        double f1 = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
//                        DecimalFormat df = new DecimalFormat("##.0000");
//                        String value = df.format(new BigDecimal(bigInteger.floatValue() / ConstantValue.bnbDecimal));
                        String value = f1 + "";
                        KLog.i(value);
                        mView.setBnbValue(value);
                        return value;
                    }
                }).subscribe();
    }

    /**
     * 查询代币余额
     */
    public static Observable<BigInteger> getTokenBalance(Web3j web3j, String fromAddress, String contractAddress) {
        return Observable.just(fromAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new io.reactivex.functions.Function<String, BigInteger>() {
                    @Override
                    public BigInteger apply(String s) throws Exception {
                        String methodName = "balanceOf";
                        List<Type> inputParameters = new ArrayList<>();
                        List<TypeReference<?>> outputParameters = new ArrayList<>();
                        Address address = new Address(fromAddress);
                        inputParameters.add(address);

                        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
                        };
                        outputParameters.add(typeReference);
                        Function function = new Function(methodName, inputParameters, outputParameters);
                        String data = FunctionEncoder.encode(function);
                        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

                        EthCall ethCall;
                        BigInteger balanceValue = BigInteger.ZERO;
                        try {
                            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
                            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
                            balanceValue = (BigInteger) results.get(0).getValue();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return balanceValue;
                    }
                });
    }
}