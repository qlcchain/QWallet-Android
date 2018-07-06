package com.stratagile.qlink.ui.activity.wordcup.contract;

import com.stratagile.qlink.entity.RaceTimes;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for GameFragment
 * @Description: $description
 * @date 2018/06/11 16:31:05
 */
public interface GameListContract {
    interface View extends BaseView<GameListContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void showRaceTimes(List<RaceTimes.DataBean> arrayList);

        void setCurrentServerTime(String time);

        void startBet(int position);
    }

    interface GameListContractPresenter extends BasePresenter {

        void getGameListFromServer(String gameStatus,int requestPage,int onePageSize);
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getRaceTimes(Map map);

        void getCurrentServerTime(Map map);
    }
}