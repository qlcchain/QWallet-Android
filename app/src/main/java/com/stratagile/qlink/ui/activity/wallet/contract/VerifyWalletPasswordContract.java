package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for VerifyWalletPasswordActivity
 * @Description: $description
 * @date 2018/01/24 18:19:18
 */
public interface VerifyWalletPasswordContract {
    interface View extends BaseView<VerifyWalletPasswordContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface VerifyWalletPasswordContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}