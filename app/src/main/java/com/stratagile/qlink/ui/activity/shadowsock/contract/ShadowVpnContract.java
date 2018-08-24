package com.stratagile.qlink.ui.activity.shadowsock.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ShadowVpnActivity
 * @Description: $description
 * @date 2018/08/07 11:54:13
 */
public interface ShadowVpnContract {
    interface View extends BaseView<ShadowVpnContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface ShadowVpnContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}