package com.stratagile.qlink.ui.activity.eth.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

/**
 * @author hzp
 * @Package The contract for ImportEthWalletActivity
 * @Description: $description
 * @date 2018/05/24 16:30:41
 */
public interface ImportEthWalletContract {
    interface View extends BaseView<ImportEthWalletContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void reportCreatedWalletSuccess();
    }

    interface ImportEthWalletContractPresenter extends BasePresenter {
        //        /**
//         *
//         */
//        void getBusinessInfo(Map map);
        void reportWalletImported(String address, String publicKey, String signData);
    }
}