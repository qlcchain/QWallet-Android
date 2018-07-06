package com.stratagile.qlink.ui.activity.mainwallet.contract;

import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.Raw;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author zl
 * @Package The contract for MainWalletActivity
 * @Description: $description
 * @date 2018/06/13 14:09:33
 */
public interface MainWalletContract {
    interface View extends BaseView<MainWalletContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        /**
         * 创建钱包成功
         */
        void onCreatWalletSuccess(Wallet wallet);

        /**
         * 获取钱包详情成功
         */
        void onGetBalancelSuccess(Balance balance);

        void onGetRawSuccess(Raw raw);

        void setBnbValue(String value);
    }

    interface MainWalletContractPresenter extends BasePresenter {
        /**
         * 创建钱包
         * @param map
         */
        void createWallet(Map map);

        /**
         * 获取钱包详情
         * @param map
         */
        void getBalance(Map map);

        void getRaw(Map map);

        void getETHWalletDetail(String address, Map map);
    }
}