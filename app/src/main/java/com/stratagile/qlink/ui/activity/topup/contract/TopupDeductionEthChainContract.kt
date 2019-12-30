package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.EthWalletInfo
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TopupDeductionEthChainActivity
 * @Description: $description
 * @date 2019/12/27 11:59:29
 */
interface TopupDeductionEthChainContract {
    interface View : BaseView<TopupDeductionEthChainContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()


        fun saveDeductionTokenTxidBack(topupOrder: TopupOrder)
        fun topupOrderStatus(topupOrder: TopupOrder)
        fun createTopupOrderError()

        fun createTopupOrderSuccess(topupOrder: TopupOrder)

        fun setMainAddress()

        fun getEthWalletBack(ethWalletInfo: EthWalletInfo)

        fun sendPayTokenSuccess(s : String)
    }

    interface TopupDeductionEthChainContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}