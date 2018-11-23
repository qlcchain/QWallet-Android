package com.stratagile.qlink.ui.activity.main.contract;

import com.stratagile.qlink.entity.Tpcs;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for MarketFragment
 * @Description: $description
 * @date 2018/10/25 15:54:02
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

        void setData(Tpcs data);
    }

    interface MarketContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getTpcs(Map map);

    }
}