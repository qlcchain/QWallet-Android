package com.stratagile.qlink.ui.activity.my.contract;

import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for LoginActivity
 * @Description: $description
 * @date 2019/04/23 10:05:31
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
        void vCodeLoginSuccess(VcodeLogin register);
    }

    interface LoginContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void login(Map map);

        void getSignInVcode(Map map);
        void vCodeLogin(Map map);
    }
}