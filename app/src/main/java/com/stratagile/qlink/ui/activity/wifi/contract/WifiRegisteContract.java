package com.stratagile.qlink.ui.activity.wifi.contract;

import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.VertifyVpn;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for WifiRegisteFragment
 * @Description: $description
 * @date 2018/01/15 11:52:32
 */
public interface WifiRegisteContract {
    interface View extends BaseView<WifiRegisteContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void registAssetSuccess();

        void onGetBalancelSuccess(Balance balance);

        void vertifyWiFiBack(VertifyVpn vertifyVpn);

        void updateAssetSuccess();
    }

    interface WifiRegisteContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void registeWifi(Map map, String ssid);

        void getBalance(Map map);

        void vertifyWiFiName(Map map);

        void updateWiFiInfo(Map map, String ssid);


    }
}