package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.otc.TradeOrderList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for ProcessFragment
 * @Description: $description
 * @date 2019/07/16 17:52:45
 */
interface ProcessContract {
    interface View : BaseView<ProcessContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setTradeOrderList(tradeOrderList: TradeOrderList)
    }

    interface ProcessContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}