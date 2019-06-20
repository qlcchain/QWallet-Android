package com.stratagile.qlink.ui.activity.qlc.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for QlcWalletCreatedActivity
 * @Description: $description
 * @date 2019/05/20 09:24:16
 */
public interface QlcWalletCreatedContract {
    interface View extends BaseView<QlcWalletCreatedContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface QlcWalletCreatedContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}