package com.stratagile.qlink.ui.activity.eth.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for EthMnemonicShowActivity
 * @Description: $description
 * @date 2018/10/23 15:34:11
 */
public interface EthMnemonicShowContract {
    interface View extends BaseView<EthMnemonicShowContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface EthMnemonicShowContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}