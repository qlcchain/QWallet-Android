package com.stratagile.qlink.ui.activity.setting.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author zl
 * @Package The contract for SelectLanguageActivityActivity
 * @Description: $description
 * @date 2018/06/26 17:11:28
 */
public interface SelectLanguageActivityContract {
    interface View extends BaseView<SelectLanguageActivityContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface SelectLanguageActivityContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}