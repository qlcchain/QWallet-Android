package com.stratagile.qlink.ui.activity.wallet.presenter;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.socks.library.KLog;
import com.stratagile.qlc.entity.AccountHistory;
import com.stratagile.qlink.constant.ConstantValue;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import qlc.mng.AccountMng;
import qlc.network.QlcClient;
import qlc.network.QlcException;
import qlc.rpc.impl.LedgerRpc;
import qlc.utils.Helper;

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
                        String data = "{\"operations\":" + baseBack.getData() + "}";
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
            transactionInfo.setTransactionState(dataBean.get(i).isSuccess() ? "success" : "failed");
            transactionInfo.setOwner(walletAddress);
            if (walletAddress.equals(dataBean.get(i).getFrom())) {
                transactionInfo.setShowAddress(dataBean.get(i).getTo());
            } else {
                transactionInfo.setShowAddress(dataBean.get(i).getFrom());
            }
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
                transactionInfo.setShowAddress(operationsBeans.get(i).getTo());
                transactionInfo.setTo(operationsBeans.get(i).getTo());
                transactionInfo.setTransactionState(operationsBeans.get(i).getType());
                transactionInfo.setOwner(address);
                if (address.equals(operationsBeans.get(i).getFrom())) {
                    transactionInfo.setShowAddress(operationsBeans.get(i).getTransactionHash());
                } else {
                    transactionInfo.setShowAddress(operationsBeans.get(i).getTransactionHash());
                }
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
                transactionInfo1.setShowAddress(operationsBeans.get(i).getTransactionHash());
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
                if (address.equals(operationsBeans.get(i).getFrom())) {
                    transactionInfo.setShowAddress(operationsBeans.get(i).getTransactionHash());
                } else {
                    transactionInfo.setShowAddress(operationsBeans.get(i).getTransactionHash());
                }
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
                            if (((String) map.get("address")).equals(baseBack.getData().get(i).getAddress_from())) {
                                transactionInfo.setShowAddress(baseBack.getData().get(i).getAddress_to());
                            } else {
                                transactionInfo.setShowAddress(baseBack.getData().get(i).getAddress_from());
                            }
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
                            transactionInfo.setOwner((String) map.get("account"));
                            if (((String) map.get("account")).equals(baseBack.getData().getData().getTrace_list().get(i).getSender())) {
                                transactionInfo.setShowAddress(baseBack.getData().getData().getTrace_list().get(i).getReceiver());
                            } else {
                                transactionInfo.setShowAddress(baseBack.getData().getData().getTrace_list().get(i).getSender());
                            }
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

    @Override
    public void getQlcAccountHistoryTopn(String account, int count, int offset) {
        JSONArray params = new JSONArray();
        params.add(account);
        params.add(count);
        params.add(offset);

        JSONArray blockHashJsonArray = new JSONArray();
        JSONArray receiveJsonArray = new JSONArray();
        try {
            ArrayList<TransactionInfo> transactionInfos = new ArrayList<>();
            QlcClient qlcClient = new QlcClient(ConstantValue.qlcNode);
            LedgerRpc rpc = new LedgerRpc(qlcClient);
            JSONObject jsonObject = rpc.accountHistoryTopn(params);
            AccountHistory accountHistory = new Gson().fromJson(jsonObject.toJSONString(), AccountHistory.class);
            for (int i = 0; i < accountHistory.getResult().size(); i++) {
                TransactionInfo transactionInfo = new TransactionInfo();
                transactionInfo.setTransactionType(AllWallet.WalletType.QlcWallet);
                transactionInfo.setShowAddress("");
                transactionInfo.setTransactionToken(accountHistory.getResult().get(i).getTokenName());
                transactionInfo.setTransactionValue(accountHistory.getResult().get(i).getAmount());
                if ("Receive".equals(accountHistory.getResult().get(i).getType()) || "Open".equals(accountHistory.getResult().get(i).getType())) {
                    transactionInfo.setFrom("unkwon");
                    transactionInfo.setTo(accountHistory.getResult().get(i).getAddress());
                    transactionInfo.setTransationHash(accountHistory.getResult().get(i).getHash());
                    transactionInfo.setShowAddress(accountHistory.getResult().get(i).getLink());
                    receiveJsonArray.add(accountHistory.getResult().get(i).getLink());
                } else if ("ContractReward".equals(accountHistory.getResult().get(i).getType())) {
                    transactionInfo.setFrom(AccountMng.publicKeyToAddress(Helper.hexStringToBytes(accountHistory.getResult().get(i).getLink())));
                    transactionInfo.setTo(accountHistory.getResult().get(i).getAddress());
                    transactionInfo.setTransationHash(AccountMng.publicKeyToAddress(Helper.hexStringToBytes(accountHistory.getResult().get(i).getHash())));
                    transactionInfo.setShowAddress(AccountMng.publicKeyToAddress(Helper.hexStringToBytes(accountHistory.getResult().get(i).getLink())));
                } else if ("ContractSend".equals(accountHistory.getResult().get(i).getType())) {
                    transactionInfo.setFrom(account);
                    transactionInfo.setTo(accountHistory.getResult().get(i).getAddress());
                    transactionInfo.setTransationHash(AccountMng.publicKeyToAddress(Helper.hexStringToBytes(accountHistory.getResult().get(i).getHash())));
                    transactionInfo.setShowAddress(accountHistory.getResult().get(i).getAddress());
                }else if ("Send".equals(accountHistory.getResult().get(i).getType())) {
                    transactionInfo.setFrom(accountHistory.getResult().get(i).getAddress());
                    transactionInfo.setTransationHash(AccountMng.publicKeyToAddress(Helper.hexStringToBytes(accountHistory.getResult().get(i).getHash())));
                    transactionInfo.setTo(AccountMng.publicKeyToAddress(Helper.hexStringToBytes(accountHistory.getResult().get(i).getLink())));
                    transactionInfo.setShowAddress(AccountMng.publicKeyToAddress(Helper.hexStringToBytes(accountHistory.getResult().get(i).getLink())));
                }
                transactionInfo.setOwner(account);
                transactionInfo.setTokenDecimals(8);
                transactionInfo.setTimestamp(accountHistory.getResult().get(i).getTimestamp());
                transactionInfos.add(transactionInfo);
            }
            blockHashJsonArray.add(receiveJsonArray);
            JSONObject sendJsonObject = rpc.blocksInfo(blockHashJsonArray);
            KLog.i(sendJsonObject);
            AccountHistory sendHistory = new Gson().fromJson(sendJsonObject.toJSONString(), AccountHistory.class);
            for (TransactionInfo transactionInfo : transactionInfos) {
                for (int i = 0; i < sendHistory.getResult().size(); i++) {
                    if (transactionInfo.getShowAddress().equals(sendHistory.getResult().get(i).getHash())) {
                        transactionInfo.setFrom(sendHistory.getResult().get(i).getAddress());
                        transactionInfo.setShowAddress(sendHistory.getResult().get(i).getAddress());
                    }
                }
            }
            mView.setEthTransactionHistory(transactionInfos);
        } catch (QlcException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(AccountMng.publicKeyToAddress(Helper.hexStringToBytes("6bad3bc9587f61bfcab2b48707d7d9fdae2bdf98c91dcb1d8fed89c3911375b3")));
        System.out.println(AccountMng.publicKeyToAddress(Helper.hexStringToBytes("d614bb9d5e20ad063316ce091148e77c99136c6194d55c7ecc7ffa9620dbcaeb")));
        System.out.println(AccountMng.publicKeyToAddress(Helper.hexStringToBytes("8f807e8ccc9a081bb035da3ecef214ed525a31529f217c7e16b883306016fb52")));
    }

    private long parseEosTransactionTimestamp(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdr.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime() / 1000;
            KLog.i(l);
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