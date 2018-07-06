package com.stratagile.qlink.ui.activity.im.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ConversationActivity
 * @Description: $description
 * @date 2018/03/19 15:49:59
 */
public interface ConversationContract {
    interface View extends BaseView<ConversationContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface ConversationContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}