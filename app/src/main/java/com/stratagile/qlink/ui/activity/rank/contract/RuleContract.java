package com.stratagile.qlink.ui.activity.rank.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for RuleActivity
 * @Description: $description
 * @date 2018/08/03 09:44:05
 */
public interface RuleContract {
    interface View extends BaseView<RuleContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface RuleContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}