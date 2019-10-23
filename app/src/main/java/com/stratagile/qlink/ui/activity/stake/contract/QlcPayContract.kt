package com.stratagile.qlink.ui.activity.stake.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for QlcPayActivity
 * @Description: $description
 * @date 2019/08/16 09:59:21
 */
interface QlcPayContract {
    interface View : BaseView<QlcPayContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface QlcPayContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}