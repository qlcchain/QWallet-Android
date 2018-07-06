package com.stratagile.qlink.ui.activity.main.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for WebViewActivity
 * @Description: $description
 * @date 2018/05/30 11:52:27
 */
public interface WebViewContract {
    interface View extends BaseView<WebViewContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface WebViewContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}