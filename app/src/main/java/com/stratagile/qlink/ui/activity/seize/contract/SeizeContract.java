package com.stratagile.qlink.ui.activity.seize.contract;

import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for SeizeActivity
 * @Description: $description
 * @date 2018/04/13 10:58:53
 */
public interface SeizeContract {
    interface View extends BaseView<SeizeContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void onGetBalancelSuccess(Balance balance);
    }

    interface SeizeContractPresenter extends BasePresenter {
        void getBalance(Map map);
    }
}