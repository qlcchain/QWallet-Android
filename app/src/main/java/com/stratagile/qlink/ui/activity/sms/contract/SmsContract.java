package com.stratagile.qlink.ui.activity.sms.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for SmsFragment
 * @Description: $description
 * @date 2018/01/10 14:59:05
 */
public interface SmsContract {
    interface View extends BaseView<SmsContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface SmsContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}