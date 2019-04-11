package com.stratagile.qlink.ui.activity.finance.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for MyProductActivity
 * @Description: $description
 * @date 2019/04/11 16:18:23
 */
public interface MyProductContract {
    interface View extends BaseView<MyProductContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface MyProductContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}