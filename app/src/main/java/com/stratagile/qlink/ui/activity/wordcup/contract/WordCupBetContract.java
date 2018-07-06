package com.stratagile.qlink.ui.activity.wordcup.contract;

import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.RaceTimes;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for WordCupBetActivity
 * @Description: $description
 * @date 2018/06/12 11:36:18
 */
public interface WordCupBetContract {
    interface View extends BaseView<WordCupBetContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void onBetSuccess(RaceTimes.DataBean betRecordBean);

        void onGetBalancelSuccess(Balance balance);
    }

    interface WordCupBetContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void betRace(Map map);

        void getBalance(Map map);
    }
}