package com.stratagile.qlink.ui.activity.vpn.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for SelectContinentActivity
 * @Description: $description
 * @date 2018/03/12 16:57:42
 */
public interface SelectContinentContract {
    interface View extends BaseView<SelectContinentContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface SelectContinentContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}