package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.otc.TradeOrderDetail
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TradeOrderDetailActivity
 * @Description: $description
 * @date 2019/07/17 11:14:13
 */
interface TradeOrderDetailContract {
    interface View : BaseView<TradeOrderDetailContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setTradeOrderDetail(tradeOrderDetail: TradeOrderDetail)

    }

    interface TradeOrderDetailContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}