package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ExportEthKeyStoreActivity
 * @Description: $description
 * @date 2018/11/06 17:23:06
 */
public interface ExportEthKeyStoreContract {
    interface View extends BaseView<ExportEthKeyStoreContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface ExportEthKeyStoreContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}