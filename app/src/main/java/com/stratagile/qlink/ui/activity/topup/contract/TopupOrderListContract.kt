package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.entity.topup.TopupOrderList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TopupOrderListActivity
 * @Description: $description
 * @date 2019/09/26 15:00:15
 */
interface TopupOrderListContract {
    interface View : BaseView<TopupOrderListContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setOrderList(topupOrderList: TopupOrderList, page : Int)

        fun cancelOrderSuccess(topupOrder: TopupOrder, position : Int)
    }

    interface TopupOrderListContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}