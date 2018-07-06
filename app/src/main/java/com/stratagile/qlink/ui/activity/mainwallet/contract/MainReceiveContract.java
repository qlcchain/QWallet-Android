package com.stratagile.qlink.ui.activity.mainwallet.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author zl
 * @Package The contract for MainReceiveFragment
 * @Description: $description
 * @date 2018/06/14 09:25:13
 */
public interface MainReceiveContract {
    interface View extends BaseView<MainReceiveContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface MainReceiveContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}