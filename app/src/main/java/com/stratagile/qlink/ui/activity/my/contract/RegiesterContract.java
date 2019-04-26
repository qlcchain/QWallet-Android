package com.stratagile.qlink.ui.activity.my.contract;

import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for RegiesterFragment
 * @Description: $description
 * @date 2019/04/09 11:45:07
 */
public interface RegiesterContract {
    interface View extends BaseView<RegiesterContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void registerSuccess(VcodeLogin register);
    }

    interface RegiesterContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getSignUpVcode(Map map);

        void register(Map map);
    }
}