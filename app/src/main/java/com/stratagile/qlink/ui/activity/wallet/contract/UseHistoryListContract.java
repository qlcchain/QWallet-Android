package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.entity.BuyQlc;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
import com.stratagile.qlink.api.transaction.SendCallBack;

import java.util.Map;

import neoutils.Wallet;

/**
 * @author hzp
 * @Package The contract for UseHistoryFragment
 * @Description: $description
 * @date 2018/01/19 11:44:00
 */
public interface UseHistoryListContract {
    interface View extends BaseView<UseHistoryListContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void buyQlcBack(BuyQlc buyQlc);
        void sendFundBack(BuyQlc buyQlc);

        void getScanPermissionSuccess();

        void getMainAddressSuccess(String mainAddress, String neo);
    }

    interface UseHistoryListContractPresenter extends BasePresenter {

        void getUseHistoryListFromServer(String useHistoryStatus, int requestPage, int onePageSize);
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void buyQlc(String exchangeId, String toAddress, String neo, String wif, String fromAddress, SendCallBack sendCallBack);

        void trasaction(String fromAddress, String address, String wif, String qlc);

        void getScanPermission();

        void getUtxo(String address, SendCallBack sendCallBack);

        void getMainAddress(String neo);

        void sendNEP5Token(Wallet wallet, String tokenContractHash, String fromAddress, String toAddress, Double amount, SendCallBack sendCallBack);

        void getRecords(Map map);
    }
}