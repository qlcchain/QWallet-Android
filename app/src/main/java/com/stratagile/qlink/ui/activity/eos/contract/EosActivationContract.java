package com.stratagile.qlink.ui.activity.eos.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for EosActivationActivity
 * @Description: $description
 * @date 2018/12/12 11:17:52
 */
public interface EosActivationContract {
    interface View extends BaseView<EosActivationContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface EosActivationContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}