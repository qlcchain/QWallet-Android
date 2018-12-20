package com.stratagile.qlink.ui.activity.main.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for GuestActivity
 * @Description: $description
 * @date 2018/06/21 15:39:34
 */
public interface GuestContract {
    interface View extends BaseView<GuestContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface GuestContractPresenter extends BasePresenter {
        /**
         *
         */
        void importWallet(Map map);
    }
}