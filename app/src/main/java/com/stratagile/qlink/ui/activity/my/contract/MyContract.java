package com.stratagile.qlink.ui.activity.my.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for MyFragment
 * @Description: $description
 * @date 2019/04/09 10:02:03
 */
public interface MyContract {
    interface View extends BaseView<MyContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface MyContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}