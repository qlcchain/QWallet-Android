package com.stratagile.qlink.ui.activity.wifi.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for RegisteWifiSuccessActivity
 * @Description: $description
 * @date 2018/01/19 19:45:43
 */
public interface RegisteWifiSuccessContract {
    interface View extends BaseView<RegisteWifiSuccessContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface RegisteWifiSuccessContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}