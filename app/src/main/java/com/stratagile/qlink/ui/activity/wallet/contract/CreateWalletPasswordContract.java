package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for CreateWalletPasswordActivity
 * @Description: $description
 * @date 2018/01/24 17:48:46
 */
public interface CreateWalletPasswordContract {
    interface View extends BaseView<CreateWalletPasswordContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface CreateWalletPasswordContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}