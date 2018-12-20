package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for ImportWalletActivity
 * @Description: $description
 * @date 2018/01/23 14:24:49
 */
public interface ImportWalletContract {
    interface View extends BaseView<ImportWalletContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void onImportWalletSuccess(CreateWallet createWallet);

        void reportCreatedWalletSuccess();
    }

    interface ImportWalletContractPresenter extends BasePresenter {
        void importWallet(Map map);

        void reportWalletCreated(String address, String blockChain, String publicKey);
    }
}