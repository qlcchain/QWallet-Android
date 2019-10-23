package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.EthWalletInfo
import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for OrderSellFragment
 * @Description: $description
 * @date 2019/07/08 17:24:46
 */
interface OrderSellContract {
    interface View : BaseView<OrderSellContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun generateSellQgasOrderSuccess()

        fun generateSellQgasOrderFailed(content : String)

        fun setNeoDetail(neoWalletInfo : NeoWalletInfo)

        fun setEthTokens(ethWalletInfo: EthWalletInfo)
    }

    interface OrderSellContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)

        fun sendQgas(amount : String, receiveAddress : String, map: MutableMap<String, String>)
    }
}