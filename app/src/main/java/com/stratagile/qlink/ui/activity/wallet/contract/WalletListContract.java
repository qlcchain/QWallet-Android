package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for WalletFragment
 * @Description: $description
 * @date 2018/01/09 15:08:22
 */
public interface WalletListContract {
    interface View extends BaseView<WalletListContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface WalletListContractPresenter extends BasePresenter {

        void getWalletListFromServer(String walletStatus,int requestPage,int onePageSize);
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}