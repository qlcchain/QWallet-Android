package com.stratagile.qlink.ui.activity.wifi.contract;

import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.qlink.QlinkEntity;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

/**
 * @author hzp
 * @Package The contract for WifiFragment
 * @Description: $description
 * @date 2018/01/09 14:02:09
 */
public interface WifiListContract {
    interface View extends BaseView<WifiListContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

//        void startSetData();
    }

    interface WifiListContractPresenter extends BasePresenter {

        void getWifiListFromServer(String wifiStatus, int requestPage, int onePageSize);
        void createLinkToWifi(WifiEntity wifiEntity);

        void handlerQlinkData(QlinkEntity qlinkEntity);
    }
}