package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for QrCodeDetailActivity
 * @Description: $description
 * @date 2018/02/28 15:16:22
 */
public interface QrCodeDetailContract {
    interface View extends BaseView<QrCodeDetailContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface QrCodeDetailContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}