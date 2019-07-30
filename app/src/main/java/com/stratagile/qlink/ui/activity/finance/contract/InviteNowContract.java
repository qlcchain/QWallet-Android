package com.stratagile.qlink.ui.activity.finance.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for InviteNowActivity
 * @Description: $description
 * @date 2019/04/28 16:32:00
 */
public interface InviteNowContract {
    interface View extends BaseView<InviteNowContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface InviteNowContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}