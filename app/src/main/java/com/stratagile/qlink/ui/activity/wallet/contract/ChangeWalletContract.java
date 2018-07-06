package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for ChangeWalletActivity
 * @Description: $description
 * @date 2018/03/05 11:36:39
 */
public interface ChangeWalletContract {
    interface View extends BaseView<ChangeWalletContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        /**
         * 获取钱包详情成功
         */
        void onGetBalancelSuccess(Balance balance);
    }

    interface ChangeWalletContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        /**
         * 获取钱包详情
         * @param map
         */
        void getBalance(Map map);
    }
}