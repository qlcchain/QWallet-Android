package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.EthWalletInfo
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TopupEthPayActivity
 * @Description: $description
 * @date 2019/10/24 10:18:43
 */
interface TopupEthPayContract {
    interface View : BaseView<TopupEthPayContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun getEthWalletBack(ethWalletInfo: EthWalletInfo)

        fun sendPayTokenSuccess(txid : String)

        fun createTopupOrderSuccess(topupOrder: TopupOrder)
        fun createTopupOrderError()
        fun topupOrderStatus(topupOrder: TopupOrder)
        fun setMainAddress()
    }

    interface TopupEthPayContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}