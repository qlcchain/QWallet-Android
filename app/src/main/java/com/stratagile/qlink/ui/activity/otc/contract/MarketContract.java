package com.stratagile.qlink.ui.activity.otc.contract;

import com.stratagile.qlink.entity.EntrustOrderList;
import com.stratagile.qlink.entity.otc.TradePair;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;

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

        void setRemoteTradePairs(ArrayList<TradePair.PairsListBean> pairsListBeans);
    }

    interface MarketContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}