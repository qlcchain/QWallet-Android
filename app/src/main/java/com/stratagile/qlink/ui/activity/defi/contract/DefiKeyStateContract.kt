package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for DefiKeyStateFragment
 * @Description: $description
 * @date 2020/06/02 10:25:39
 */
interface DefiKeyStateContract {
    interface View : BaseView<DefiKeyStateContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface DefiKeyStateContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}