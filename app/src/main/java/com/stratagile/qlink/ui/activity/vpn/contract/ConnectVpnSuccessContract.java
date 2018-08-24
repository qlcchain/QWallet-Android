package com.stratagile.qlink.ui.activity.vpn.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ConnectVpnSuccessActivity
 * @Description: $description
 * @date 2018/02/09 10:28:44
 */
public interface ConnectVpnSuccessContract {
    interface View extends BaseView<ConnectVpnSuccessContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface ConnectVpnSuccessContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}