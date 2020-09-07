package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for SelectWalletTypeActivity
 * @Description: $description
 * @date 2018/10/19 10:51:45
 */
public interface SelectWalletTypeContract {
    interface View extends BaseView<SelectWalletTypeContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void createEthWalletSuccess(EthWallet ethWallet);

        void createNeoWalletSuccess(Wallet wallet);

        void reportCreatedWalletSuccess();

        void createQlcWalletSuccess();
    }

    interface SelectWalletTypeContractPresenter extends BasePresenter {
        void createEthWallet();

        void createNeoWallet();
        void createQlcWallet();
    }
}