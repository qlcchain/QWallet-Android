package com.stratagile.qlink.ui.activity.my.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for SelectCountryActivity
 * @Description: $description
 * @date 2019/04/09 14:38:53
 */
public interface SelectCountryContract {
    interface View extends BaseView<SelectCountryContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface SelectCountryContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getSignUpVcode(Map map);
    }
}