package com.stratagile.qlink.ui.activity.wifi.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for WifiShareFragment
 * @Description: $description
 * @date 2018/01/15 11:52:51
 */
public interface WifiShareContract {
    interface View extends BaseView<WifiShareContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface WifiShareContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}