package com.stratagile.qlink.ui.activity.seize.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for SeizeVpnActivity
 * @Description: $description
 * @date 2018/04/13 12:08:31
 */
public interface SeizeVpnContract {
    interface View extends BaseView<SeizeVpnContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void registVpnSuccess();
    }

    interface SeizeVpnContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void registerVpn(Map map, String vpnName);
    }
}