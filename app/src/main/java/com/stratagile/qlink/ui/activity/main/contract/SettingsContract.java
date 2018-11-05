package com.stratagile.qlink.ui.activity.main.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for SettingsFragment
 * @Description: $description
 * @date 2018/10/29 10:38:15
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
    }

    interface SettingsContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}