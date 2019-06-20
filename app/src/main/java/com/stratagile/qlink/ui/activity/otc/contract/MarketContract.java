package com.stratagile.qlink.ui.activity.otc.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for MarketFragment
 * @Description: $description
 * @date 2019/06/14 16:23:19
 */
public interface MarketContract {
    interface View extends BaseView<MarketContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface MarketContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}