package com.stratagile.qlink.ui.activity.my.contract;

import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for LoginFragment
 * @Description: $description
 * @date 2019/04/09 11:45:22
 */
public interface LoginContract {
    interface View extends BaseView<LoginContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void loginSuccess(Register register);
    }

    interface LoginContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
        void login(Map map);
    }
}