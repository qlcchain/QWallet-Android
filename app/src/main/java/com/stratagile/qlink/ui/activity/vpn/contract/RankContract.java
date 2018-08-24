package com.stratagile.qlink.ui.activity.vpn.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for RankActivity
 * @Description: $description
 * @date 2018/07/31 17:14:45
 */
public interface RankContract {
    interface View extends BaseView<RankContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface RankContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}