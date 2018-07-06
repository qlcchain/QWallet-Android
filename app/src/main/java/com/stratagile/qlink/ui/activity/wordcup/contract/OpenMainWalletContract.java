package com.stratagile.qlink.ui.activity.wordcup.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author zl
 * @Package The contract for OpenMainWalletFragment
 * @Description: $description
 * @date 2018/06/13 17:37:05
 */
public interface OpenMainWalletContract {
    interface View extends BaseView<OpenMainWalletContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface OpenMainWalletContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}