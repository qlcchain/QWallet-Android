package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.otc.TradeOrderList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for CompleteFragment
 * @Description: $description
 * @date 2019/07/16 17:53:03
 */
interface CompleteContract {
    interface View : BaseView<CompleteContractPresenter> {
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

    interface CompleteContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}