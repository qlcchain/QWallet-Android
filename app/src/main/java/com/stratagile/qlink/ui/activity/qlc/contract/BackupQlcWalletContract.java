package com.stratagile.qlink.ui.activity.qlc.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for BackupQlcWalletActivity
 * @Description: $description
 * @date 2019/05/20 10:52:55
 */
public interface BackupQlcWalletContract {
    interface View extends BaseView<BackupQlcWalletContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface BackupQlcWalletContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}