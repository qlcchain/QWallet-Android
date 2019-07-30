package com.stratagile.qlink.ui.activity.finance.contract;

import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.newwinq.Order;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for MyProductActivity
 * @Description: $description
 * @date 2019/04/11 16:18:23
 */
public interface MyProductContract {
    interface View extends BaseView<MyProductContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void getOrderBack(Order order);

        void redeemOrderBack(BaseBack baseBack);
    }

    interface MyProductContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getOrderList(Map map);

        void redeemOrder(Map map);
    }
}