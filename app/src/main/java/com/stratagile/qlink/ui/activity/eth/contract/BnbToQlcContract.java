package com.stratagile.qlink.ui.activity.eth.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for BnbToQlcActivity
 * @Description: $description
 * @date 2018/05/24 10:46:37
 */
public interface BnbToQlcContract {
    interface View extends BaseView<BnbToQlcContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void startTransaction(String transactionHash);

        void transactionSuccess(String recordId);
    }

    interface BnbToQlcContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void parseWallet(String password, String address, String amount);

        void startBnb2Qlc(Map map);
    }
}