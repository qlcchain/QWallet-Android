package com.stratagile.qlink.ui.activity.qlc.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for QlcTransferActivity
 * @Description: $description
 * @date 2019/05/20 18:05:32
 */
public interface QlcTransferContract {
    interface View extends BaseView<QlcTransferContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface QlcTransferContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}