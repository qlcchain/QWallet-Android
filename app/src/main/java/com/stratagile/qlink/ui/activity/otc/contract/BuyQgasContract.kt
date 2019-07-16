package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.otc.EntrustOrderInfo
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for BuyQgasActivity
 * @Description: $description
 * @date 2019/07/09 14:18:25
 */
interface BuyQgasContract {
    interface View : BaseView<BuyQgasContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun generateTradeBuyQgasOrderSuccess()

        fun setEntrustOrder(entrustOrderInfo: EntrustOrderInfo)
    }

    interface BuyQgasContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
        fun generateTradeBuyQgasOrder(map: MutableMap<String, String>)
    }
}