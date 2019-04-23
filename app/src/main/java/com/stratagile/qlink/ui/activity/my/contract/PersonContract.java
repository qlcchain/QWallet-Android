package com.stratagile.qlink.ui.activity.my.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for PersonActivity
 * @Description: $description
 * @date 2019/04/22 14:28:46
 */
public interface PersonContract {
    interface View extends BaseView<PersonContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface PersonContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}