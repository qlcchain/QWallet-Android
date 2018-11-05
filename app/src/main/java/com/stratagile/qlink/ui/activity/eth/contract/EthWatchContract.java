package com.stratagile.qlink.ui.activity.eth.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for EthWatchFragment
 * @Description: $description
 * @date 2018/10/22 14:16:26
 */
public interface EthWatchContract {
    interface View extends BaseView<EthWatchContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface EthWatchContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}