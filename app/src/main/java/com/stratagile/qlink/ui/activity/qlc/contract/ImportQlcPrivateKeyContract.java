package com.stratagile.qlink.ui.activity.qlc.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ImportQlcPrivateKeyFragment
 * @Description: $description
 * @date 2019/05/21 09:46:08
 */
public interface ImportQlcPrivateKeyContract {
    interface View extends BaseView<ImportQlcPrivateKeyContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface ImportQlcPrivateKeyContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}