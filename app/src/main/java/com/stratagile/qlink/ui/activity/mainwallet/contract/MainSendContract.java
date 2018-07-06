package com.stratagile.qlink.ui.activity.mainwallet.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author zl
 * @Package The contract for MainSendFragment
 * @Description: $description
 * @date 2018/06/14 09:24:50
 */
public interface MainSendContract {
    interface View extends BaseView<MainSendContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void getScanPermissionSuccess();
    }

    interface MainSendContractPresenter extends BasePresenter {
        void getScanPermission();
    }
}