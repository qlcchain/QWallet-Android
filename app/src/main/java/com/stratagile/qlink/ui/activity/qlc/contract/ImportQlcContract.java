package com.stratagile.qlink.ui.activity.qlc.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ImportQlcActivity
 * @Description: $description
 * @date 2019/05/21 09:40:38
 */
public interface ImportQlcContract {
    interface View extends BaseView<ImportQlcContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface ImportQlcContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}