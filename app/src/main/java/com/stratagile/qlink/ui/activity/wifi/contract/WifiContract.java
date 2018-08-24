package com.stratagile.qlink.ui.activity.wifi.contract;

import android.net.wifi.ScanResult;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for WifiFragment
 * @Description: $description
 * @date 2018/01/09 13:46:43
 */
public interface WifiContract {
    interface View extends BaseView<WifiContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void startUpdateWIfiInfo();

        void setPager(int position);

    }

    interface WifiContractPresenter extends BasePresenter {
        void getRegistedSsid();

        void requestPermission();

        void handlerWifiChange(List<ScanResult> wifiList);

        void clearData();
    }
}