package com.stratagile.qlink.ui.activity.setting.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for SettingsActivity
 * @Description: $description
 * @date 2018/05/29 09:30:35
 */
public interface SettingsContract {
    interface View extends BaseView<SettingsContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void logoutSuccess();
    }

    interface SettingsContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}