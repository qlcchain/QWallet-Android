package com.stratagile.qlink.ui.activity.rank.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for RankListFragment
 * @Description: $description
 * @date 2018/07/31 18:09:12
 */
public interface RankListContract {
    interface View extends BaseView<RankListContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface RankListContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}