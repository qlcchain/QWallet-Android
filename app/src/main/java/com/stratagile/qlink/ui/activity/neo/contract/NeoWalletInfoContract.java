package com.stratagile.qlink.ui.activity.neo.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for NeoWalletInfoActivity
 * @Description: $description
 * @date 2018/11/05 17:19:51
 */
public interface NeoWalletInfoContract {
    interface View extends BaseView<NeoWalletInfoContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface NeoWalletInfoContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}