package com.stratagile.qlink.ui.activity.qlc.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;
/**
 * @author hzp
 * @Package The contract for ImportQlcSeedFragment
 * @Description: $description
 * @date 2019/05/21 09:46:27
 */
public interface ImportQlcSeedContract {
    interface View extends BaseView<ImportQlcSeedContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface ImportQlcSeedContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);
    }
}