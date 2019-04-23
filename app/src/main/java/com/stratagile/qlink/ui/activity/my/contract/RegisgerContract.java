package com.stratagile.qlink.ui.activity.my.contract;

import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for RegisgerActivity
 * @Description: $description
 * @date 2019/04/23 12:02:02
 */
public interface RegisgerContract {
    interface View extends BaseView<RegisgerContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
        void registerSuccess(Register register);
    }

    interface RegisgerContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getSignUpVcode(Map map);

        void register(Map map);
    }
}