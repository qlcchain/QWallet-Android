package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for OrderBuyFragment
 * @Description: $description
 * @date 2019/07/08 17:24:58
 */
interface OrderBuyContract {
    interface View : BaseView<OrderBuyContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun generateBuyQgasOrderSuccess()
    }

    interface OrderBuyContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}