package com.stratagile.qlink.ui.activity.wifi.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for RegisterWifiActivity
 * @Description: $description
 * @date 2018/01/09 17:28:09
 */
public interface RegisterWifiContract {
    interface View extends BaseView<RegisterWifiContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void registeWifiSuccess();
    }

    interface RegisterWifiContractPresenter extends BasePresenter {
        void registWIfi(Map map);
    }
}