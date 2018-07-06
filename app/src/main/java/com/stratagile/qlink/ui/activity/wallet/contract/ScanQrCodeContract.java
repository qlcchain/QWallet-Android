package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ScanQrCodeActivity
 * @Description: $description
 * @date 2018/03/05 17:42:26
 */
public interface ScanQrCodeContract {
    interface View extends BaseView<ScanQrCodeContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface ScanQrCodeContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}