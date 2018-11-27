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
import com.stratagile.qlink.entity.EosAccountTransaction;
import com.stratagile.qlink.entity.EthWalletTransaction;
import com.stratagile.qlink.entity.KLine;
import com.stratagile.qlink.entity.NeoWalletTransactionHistory;
import com.stratagile.qlink.entity.OnlyEthTransactionHistory;
import com.stratagile.qlink.entity.TokenPrice;
import com.stratagile.qlink.entity.TransactionInfo;
import com.stratagile.qlink.ui.activity.wallet.contract.EthTransactionRecordContract;
import com.stratagile.qlink.ui.activity.wallet.EthTransactionRecordActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
                        KLog.i(baseBack.getData());
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

    @Override
    public void getOnlyEthTransaction(Map map, String walletAddress) {
        Disposable disposable = httpAPIWrapper.getOnlyEthTransaction(map)
                .subscribe(new Consumer<OnlyEthTransactionHistory>() {
                    @Override
                    public void accept(OnlyEthTransactionHistory baseBack) throws Exception {
                        //isSuccesse
                        String data = "{\"operations\":"  + baseBack.getData() + "}";
                        KLog.i(data);
                        OnlyEthTransactionHistory.EthTransactionBean ethTransactionBean = new Gson().fromJson(data, OnlyEthTransactionHistory.EthTransactionBean.class);
                        getOnlyEthTransactionHistory(ethTransactionBean.getOperations(), walletAddress);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
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

    private void getOnlyEthTransactionHistory(List<OnlyEthTransactionHistory.EthTransactionBean.OperationsBean> dataBean, String walletAddress) {
        ArrayList<TransactionInfo> transactionInfos = new ArrayList<>();
        for (int i = 0; i < dataBean.size(); i++) {
            TransactionInfo transactionInfo = new TransactionInfo();
            transactionInfo.setTransactionType(AllWallet.WalletType.EthWallet);
            transactionInfo.setTransactionToken("eth");
            transactionInfo.setTransactionValue(dataBean.get(i).getValue() + "");
            transactionInfo.setTransationHash(dataBean.get(i).getHash());
            transactionInfo.setFrom(dataBean.get(i).getFrom());
            transactionInfo.setTo(dataBean.get(i).getTo());
            transactionInfo.setTransactionState(dataBean.get(i).isSuccess()? "success" : "failed");
            transactionInfo.setOwner(walletAddress);
            transactionInfo.setTimestamp(dataBean.get(i).getTimestamp());
            transactionInfo.setTokenDecimals(0);
            transactionInfos.add(transactionInfo);
        }
        mView.setEthTransactionHistory(transactionInfos);
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

    @Override
    public void getEosAccountTransaction(String account, Map map) {
        Disposable disposable = httpAPIWrapper.getEosAccountTransaction(map)
                .subscribe(new Consumer<EosAccountTransaction>() {
                    @Override
                    public void accept(EosAccountTransaction baseBack) throws Exception {
                        //isSuccesse
                        ArrayList<TransactionInfo> transactionInfos = new ArrayList<>();
                        for (int i = 0; i < baseBack.getData().getData().getTrace_list().size(); i++) {
                            if (!baseBack.getData().getData().getTrace_list().get(i).getSender().equals(account) && !baseBack.getData().getData().getTrace_list().get(i).getReceiver().equals(account)) {
                                continue;
                            }
                            TransactionInfo transactionInfo = new TransactionInfo();
                            transactionInfo.setTransactionType(AllWallet.WalletType.EosWallet);
                            transactionInfo.setTransactionToken(baseBack.getData().getData().getTrace_list().get(i).getSymbol());
                            transactionInfo.setTransactionValue(baseBack.getData().getData().getTrace_list().get(i).getQuantity());
                            transactionInfo.setTransationHash(baseBack.getData().getData().getTrace_list().get(i).getTrx_id());
                            transactionInfo.setFrom(baseBack.getData().getData().getTrace_list().get(i).getSender());
                            transactionInfo.setTo(baseBack.getData().getData().getTrace_list().get(i).getReceiver());
                            transactionInfo.setTransactionState(baseBack.getData().getData().getTrace_list().get(i).getStatus());
                            transactionInfo.setOwner((String) map.get("address"));
                            transactionInfo.setTimestamp(parseEosTransactionTimestamp(baseBack.getData().getData().getTrace_list().get(i).getTimestamp()));
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

    private long parseEosTransactionTimestamp(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time.replace("T", " "));
            long l = date.getTime();
//            KLog.i(l);
            return l;
        } catch (Exception e) {
            e.printStackTrace();
            return new Long("123");
        }
    }

    private void getChartData(KLine baseBack, double tokenPrice) {

        mView.setChartData(baseBack);
        // set data
    }

}