package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.otc.EntrustOrderInfo
import com.stratagile.qlink.entity.otc.TradeOrderList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for OrderDetailActivity
 * @Description: $description
 * @date 2019/07/10 10:03:30
 */
interface OrderDetailContract {
    interface View : BaseView<OrderDetailContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setEntrustOrder(entrustOrderInfo: EntrustOrderInfo)

        fun setTradeOrderList(tradeOrderList: TradeOrderList)

        fun revokeOrderSuccess()
    }

    interface OrderDetailContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}