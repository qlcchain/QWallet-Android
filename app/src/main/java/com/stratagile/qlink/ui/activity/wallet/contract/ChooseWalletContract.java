package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ChooseWalletActivity
 * @Description: $description
 * @date 2018/11/06 11:21:13
 */
public interface ChooseWalletContract {
    interface View extends BaseView<ChooseWalletContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface ChooseWalletContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getEosAccountInfo(EosAccount eosAccount);
    }
}