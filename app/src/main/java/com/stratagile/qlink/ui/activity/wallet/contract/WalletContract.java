package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.Raw;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for WalletFragment
 * @Description: $description
 * @date 2018/01/18 19:08:00
 */
public interface WalletContract {
    interface View extends BaseView<WalletContractPresenter> {
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

        void onGetRawError();

        void setBnbValue(String value);
    }

    interface WalletContractPresenter extends BasePresenter {

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