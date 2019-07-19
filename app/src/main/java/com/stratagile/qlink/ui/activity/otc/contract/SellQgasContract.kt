package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.otc.EntrustOrderInfo
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for SellQgasActivity
 * @Description: $description
 * @date 2019/07/09 14:18:11
 */
interface SellQgasContract {
    interface View : BaseView<SellQgasContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
        fun setEntrustOrder(entrustOrderInfo: EntrustOrderInfo)

        fun generateBuyQgasOrderSuccess()

        fun generateTradeSellQgasOrderSuccess()
    }

    interface SellQgasContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}