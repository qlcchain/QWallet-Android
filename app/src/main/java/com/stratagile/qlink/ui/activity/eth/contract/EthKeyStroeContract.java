package com.stratagile.qlink.ui.activity.eth.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for EthKeyStroeFragment
 * @Description: $description
 * @date 2018/05/24 17:48:34
 */
public interface EthKeyStroeContract {
    interface View extends BaseView<EthKeyStroeContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface EthKeyStroeContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}