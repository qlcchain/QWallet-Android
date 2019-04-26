package com.stratagile.qlink.ui.activity.my.contract;

import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for RetrievePasswordActivity
 * @Description: $description
 * @date 2019/04/09 14:21:19
 */
public interface RetrievePasswordContract {
    interface View extends BaseView<RetrievePasswordContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void resetPasswordBack(VcodeLogin register);
    }

    interface RetrievePasswordContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getForgetPasswordVcode(Map map);

        void resetPassword(Map map);
    }
}