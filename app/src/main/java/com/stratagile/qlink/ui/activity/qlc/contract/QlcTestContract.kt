package com.stratagile.qlink.ui.activity.qlc.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for QlcTestActivity
 * @Description: $description
 * @date 2019/05/05 16:24:30
 */
interface QlcTestContract {
    interface View : BaseView<QlcTestContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface QlcTestContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}