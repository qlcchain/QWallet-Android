package com.stratagile.qlink.ui.activity.wifi.contract;

import android.content.Context;

import com.stratagile.qlink.entity.wifi.WifipasswordRsp;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ConnectWifiConfirmActivity
 * @Description: $description
 * @date 2018/01/19 19:46:26
 */
public interface ConnectWifiConfirmContract {
    interface View extends BaseView<ConnectWifiConfirmContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        Context getContext();

        void connectWifiSuccess(String msg, String ssid);

        void conncetWifiFailure(String msg);
    }

    interface ConnectWifiConfirmContractPresenter extends BasePresenter {
        void createLinkToWifi(WifipasswordRsp wifipasswordRsp);
        void getWifiPassword();

    }
}