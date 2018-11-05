package com.stratagile.qlink.ui.activity.test.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView

/**
 * @author hzp
 * @Package The contract for TestFragment
 * @Description: $description
 * @date 2018/09/10 16:51:54
 */
interface TestContract {
    interface View : BaseView<TestContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface TestContractPresenter : BasePresenter//        /**
    //         *
    //         */
    //        void getBusinessInfo(Map map);
}