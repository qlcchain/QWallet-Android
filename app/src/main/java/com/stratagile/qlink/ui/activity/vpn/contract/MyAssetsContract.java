package com.stratagile.qlink.ui.activity.vpn.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for MyAssetsActivity
 * @Description: $description
 * @date 2018/11/11 21:42:25
 */
public interface MyAssetsContract {
    interface View extends BaseView<MyAssetsContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface MyAssetsContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}