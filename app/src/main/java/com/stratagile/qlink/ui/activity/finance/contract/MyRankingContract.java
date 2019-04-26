package com.stratagile.qlink.ui.activity.finance.contract;

import com.stratagile.qlink.entity.finance.MyRanking;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for MyRankingActivity
 * @Description: $description
 * @date 2019/04/24 11:14:23
 */
public interface MyRankingContract {
    interface View extends BaseView<MyRankingContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void setData(MyRanking myRanking);
    }

    interface MyRankingContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getRanking(Map map);
    }
}