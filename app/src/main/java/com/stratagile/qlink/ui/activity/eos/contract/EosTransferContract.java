package com.stratagile.qlink.ui.activity.eos.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for EosTransferActivity
 * @Description: $description
 * @date 2018/11/27 14:27:47
 */
public interface EosTransferContract {
    interface View extends BaseView<EosTransferContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface EosTransferContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}