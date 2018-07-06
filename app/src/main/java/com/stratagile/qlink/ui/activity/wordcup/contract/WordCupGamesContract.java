package com.stratagile.qlink.ui.activity.wordcup.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for WordCupGamesActivity
 * @Description: $description
 * @date 2018/06/11 14:38:56
 */
public interface WordCupGamesContract {
    interface View extends BaseView<WordCupGamesContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface WordCupGamesContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

    }
}