package com.stratagile.qlink.ui.activity.vpn.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author zl
 * @Package The contract for RegisteWindowVpnActivityActivity
 * @Description: $description
 * @date 2018/08/03 11:56:07
 */
public interface RegisteWindowVpnActivityContract {
    interface View extends BaseView<RegisteWindowVpnActivityContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface RegisteWindowVpnActivityContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}