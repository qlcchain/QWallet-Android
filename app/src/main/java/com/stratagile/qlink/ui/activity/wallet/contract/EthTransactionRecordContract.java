package com.stratagile.qlink.ui.activity.wallet.contract;

import com.github.mikephil.charting.data.LineData;
import com.stratagile.qlink.entity.KLine;
import com.stratagile.qlink.entity.TransactionInfo;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for EthTransactionRecordActivity
 * @Description: $description
 * @date 2018/10/29 16:12:21
 */
public interface EthTransactionRecordContract {
    interface View extends BaseView<EthTransactionRecordContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void setEthTransactionHistory(ArrayList<TransactionInfo> transactionInfos);

        void setChartData(KLine data);
    }

    interface EthTransactionRecordContractPresenter extends BasePresenter {
        void getEthWalletTransaction(Map map, String walletAddress);
        void getNeoWalletTransaction(Map map);
        void getOnlyEthTransaction(Map map, String walletAddress);
        void getTokenKline(Map map, double price);
    }
}