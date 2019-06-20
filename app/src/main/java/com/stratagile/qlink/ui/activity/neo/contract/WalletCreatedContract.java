package com.stratagile.qlink.ui.activity.neo.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for WalletCreatedActivity
 * @Description: $description
 * @date 2018/01/24 16:27:17
 */
public interface WalletCreatedContract {
    interface View extends BaseView<WalletCreatedContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface WalletCreatedContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}