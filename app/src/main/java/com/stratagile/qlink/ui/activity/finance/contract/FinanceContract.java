package com.stratagile.qlink.ui.activity.finance.contract;

import com.stratagile.qlink.entity.newwinq.Product;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for FinanceFragment
 * @Description: $description
 * @date 2019/04/08 17:36:49
 */
public interface FinanceContract {
    interface View extends BaseView<FinanceContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void getProductBack(Product product);
    }

    interface FinanceContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getProductList(Map map);
    }
}