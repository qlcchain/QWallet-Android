package com.stratagile.qlink.ui.activity.vpn.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for RegisterVpnSuccessActivity
 * @Description: $description
 * @date 2018/02/11 16:34:20
 */
public interface RegisterVpnSuccessContract {
    interface View extends BaseView<RegisterVpnSuccessContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface RegisterVpnSuccessContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}