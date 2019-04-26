package com.stratagile.qlink.ui.activity.my.contract;

import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for Login1Fragment
 * @Description: $description
 * @date 2019/04/24 18:02:10
 */
public interface Login1Contract {
    interface View extends BaseView<Login1ContractPresenter> {
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

    interface Login1ContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void login(Map map);

        void getSignInVcode(Map map);
        void vCodeLogin(Map map);
    }
}