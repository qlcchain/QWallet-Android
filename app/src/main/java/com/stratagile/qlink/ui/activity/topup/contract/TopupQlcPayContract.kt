package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TopupQlcPayActivity
 * @Description: $description
 * @date 2019/09/26 10:08:40
 */
interface TopupQlcPayContract {
    interface View : BaseView<TopupQlcPayContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun createTopupOrderSuccess(topupOrder: TopupOrder)
        fun topupOrderStatus(topupOrder: TopupOrder)

        fun createTopupOrderError()

        fun setMainAddress()
    }

    interface TopupQlcPayContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}