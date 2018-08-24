package com.stratagile.qlink.ui.activity.wifi.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ConnectWifiSuccessActivity
 * @Description: $description
 * @date 2018/01/19 19:46:50
 */
public interface ConnectWifiSuccessContract {
    interface View extends BaseView<ConnectWifiSuccessContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface ConnectWifiSuccessContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}