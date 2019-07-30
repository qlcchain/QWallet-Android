package com.stratagile.qlink.ui.activity.finance.contract;

import com.stratagile.qlink.entity.finance.EarnRank;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for EarnRankActivity
 * @Description: $description
 * @date 2019/04/24 11:27:01
 */
public interface EarnRankContract {
    interface View extends BaseView<EarnRankContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void setData(EarnRank earnRank);
    }

    interface EarnRankContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getEarnRank(Map map);
    }
}