package com.stratagile.qlink.ui.activity.wifi.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for EnterWifiPasswordActivity
 * @Description: $description
 * @date 2018/02/01 13:26:40
 */
public interface EnterWifiPasswordContract {
    interface View extends BaseView<EnterWifiPasswordContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface EnterWifiPasswordContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}