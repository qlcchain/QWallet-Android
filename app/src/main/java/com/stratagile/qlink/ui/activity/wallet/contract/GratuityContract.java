package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.Reward;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for GratuityActivity
 * @Description: $description
 * @date 2018/02/02 16:19:02
 */
public interface GratuityContract {
    interface View extends BaseView<GratuityContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void onGetBalancelSuccess(Balance balance);

        void rewardBack(Reward reward);


    }

    interface GratuityContractPresenter extends BasePresenter {
        void getBalance(Map map);

        void reward(Map map, Wallet wallet, String qlc, String toAddress);
    }
}