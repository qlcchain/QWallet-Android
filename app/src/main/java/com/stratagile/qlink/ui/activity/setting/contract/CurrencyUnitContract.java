package com.stratagile.qlink.ui.activity.setting.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for CurrencyUnitActivity
 * @Description: $description
 * @date 2018/10/29 14:00:24
 */
public interface CurrencyUnitContract {
    interface View extends BaseView<CurrencyUnitContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface CurrencyUnitContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}