package com.stratagile.qlink.ui.activity.wallet.presenter;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.EthWalletTransaction;
import com.stratagile.qlink.entity.KLine;
import com.stratagile.qlink.entity.NeoWalletTransactionHistory;
import com.stratagile.qlink.entity.TokenPrice;
import com.stratagile.qlink.entity.TransactionInfo;
import com.stratagile.qlink.ui.activity.wallet.contract.EthTransactionRecordContract;
import com.stratagile.qlink.ui.activity.wallet.EthTransactionRecordActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of EthTransactionRecordActivity
 * @date 2018/10/29 16:12:21
 */
public class EthTransactionRecordPresenter implements EthTransactionRecordContract.EthTransactionRecordContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final EthTransactionRecordContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private EthTransactionRecordActivity mActivity;

    @Inject
    public EthTransactionRecordPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull EthTransactionRecordContract.View view, EthTransactionRecordActivity activity) {
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
    public void getEthWalletTransaction(Map map, String walletAddress) {
        Disposable disposable = httpAPIWrapper.getEthWalletTransaction(map)
                .subscribe(new Consumer<EthWalletTransaction>() {
                    @Override
                    public void accept(EthWalletTransaction baseBack) throws Exception {
                        //isSuccesse
                        EthWalletTransaction.EthTransactionBean ethTransactionBean = new Gson().fromJson(baseBack.getData(), EthWalletTransaction.EthTransactionBean.class);
                        getEthTransactionInfo(ethTransactionBean.getOperations(), walletAddress);

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

    private void getEthTransactionInfo(List<EthWalletTransaction.EthTransactionBean.OperationsBean> operationsBeans, String address) {
        ArrayList<TransactionInfo> transactionInfos = new ArrayList<>();
        for (int i = 0; i < operationsBeans.size(); i++) {
            if (operationsBeans.get(i).getFrom().equals(operationsBeans.get(i).getTo())) {
                TransactionInfo transactionInfo = new TransactionInfo();
                transactionInfo.setTransactionType(AllWallet.WalletType.EthWallet);
                transactionInfo.setTransactionToken(operationsBeans.get(i).getTokenInfo().getSymbol());
                transactionInfo.setTransactionValue(operationsBeans.get(i).getValue());
                transactionInfo.setTransationHash(operationsBeans.get(i).getTransactionHash());
                transactionInfo.setFrom("");
                transactionInfo.setTo(operationsBeans.get(i).getTo());
                transactionInfo.setTransactionState(operationsBeans.get(i).getType());
                transactionInfo.setOwner(address);
                transactionInfo.setTimestamp(operationsBeans.get(i).getTimestamp());
                transactionInfo.setTokenDecimals(Integer.parseInt(operationsBeans.get(i).getTokenInfo().getDecimals()));
                transactionInfos.add(transactionInfo);

                TransactionInfo transactionInfo1 = new TransactionInfo();
                transactionInfo1.setTransactionType(AllWallet.WalletType.EthWallet);
                transactionInfo1.setTransactionToken(operationsBeans.get(i).getTokenInfo().getSymbol());
                transactionInfo1.setTransactionValue(operationsBeans.get(i).getValue());
                transactionInfo1.setTransationHash(operationsBeans.get(i).getTransactionHash());
                transactionInfo1.setFrom(operationsBeans.get(i).getFrom());
                transactionInfo1.setTo("");
                transactionInfo1.setTransactionState(operationsBeans.get(i).getType());
                transactionInfo1.setOwner(address);
                transactionInfo1.setTimestamp(operationsBeans.get(i).getTimestamp());
                transactionInfo1.setTokenDecimals(Integer.parseInt(operationsBeans.get(i).getTokenInfo().getDecimals()));
                transactionInfos.add(transactionInfo1);
            } else {
                TransactionInfo transactionInfo = new TransactionInfo();
                transactionInfo.setTransactionType(AllWallet.WalletType.EthWallet);
                transactionInfo.setTransactionToken(operationsBeans.get(i).getTokenInfo().getSymbol());
                transactionInfo.setTransactionValue(operationsBeans.get(i).getValue());
                transactionInfo.setTransationHash(operationsBeans.get(i).getTransactionHash());
                transactionInfo.setFrom(operationsBeans.get(i).getFrom());
                transactionInfo.setTo(operationsBeans.get(i).getTo());
                transactionInfo.setTransactionState(operationsBeans.get(i).getType());
                transactionInfo.setOwner(address);
                transactionInfo.setTimestamp(operationsBeans.get(i).getTimestamp());
                transactionInfo.setTokenDecimals(Integer.parseInt(operationsBeans.get(i).getTokenInfo().getDecimals()));
                transactionInfos.add(transactionInfo);
            }
        }
        mView.setEthTransactionHistory(transactionInfos);
    }

    @Override
    public void getNeoWalletTransaction(Map map) {
        Disposable disposable = httpAPIWrapper.getNeoWalletTransaction(map)
                .subscribe(new Consumer<NeoWalletTransactionHistory>() {
                    @Override
                    public void accept(NeoWalletTransactionHistory baseBack) throws Exception {
                        //isSuccesse
                        ArrayList<TransactionInfo> transactionInfos = new ArrayList<>();
                        for (int i = 0; i < baseBack.getData().size(); i++) {
                            if (baseBack.getData().get(i).getAddress_from().equals(baseBack.getData().get(i).getAddress_to())) {
                                continue;
                            }
                            TransactionInfo transactionInfo = new TransactionInfo();
                            transactionInfo.setTransactionType(AllWallet.WalletType.NeoWallet);
                            transactionInfo.setTransactionToken(baseBack.getData().get(i).getSymbol());
                            transactionInfo.setTransactionValue(baseBack.getData().get(i).getAmount());
                            transactionInfo.setTransationHash(baseBack.getData().get(i).getTxid());
                            transactionInfo.setFrom(baseBack.getData().get(i).getAddress_from());
                            transactionInfo.setTo(baseBack.getData().get(i).getAddress_to());
                            transactionInfo.setTransactionState("success");
                            transactionInfo.setOwner((String) map.get("address"));
                            transactionInfo.setTimestamp(baseBack.getData().get(i).getTime());
                            transactionInfos.add(transactionInfo);
                        }
                        mView.setEthTransactionHistory(transactionInfos);
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
    public void getTokenKline(Map map, double tokenPrice) {
        Disposable disposable = httpAPIWrapper.getTokenKLine(map)
                .subscribe(new Consumer<KLine>() {
                    @Override
                    public void accept(KLine baseBack) throws Exception {
                        //isSuccesse
                        getChartData(baseBack, tokenPrice);
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

    private void getChartData(KLine baseBack, double tokenPrice) {

        mView.setChartData(baseBack);
        // set data
    }

}