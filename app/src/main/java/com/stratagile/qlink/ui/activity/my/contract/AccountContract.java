package com.stratagile.qlink.ui.activity.my.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for AccountActivity
 * @Description: $description
 * @date 2019/04/09 11:31:42
 */
public interface AccountContract {
    interface View extends BaseView<AccountContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface AccountContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}