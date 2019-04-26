package com.stratagile.qlink.ui.activity.finance.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for JoinCommunityActivity
 * @Description: $description
 * @date 2019/04/24 17:15:42
 */
public interface JoinCommunityContract {
    interface View extends BaseView<JoinCommunityContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface JoinCommunityContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}