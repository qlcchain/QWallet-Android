package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.EthWalletInfo
import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for UsdtPayActivity
 * @Description: $description
 * @date 2019/07/31 13:58:11
 */
interface UsdtPayContract {
    interface View : BaseView<UsdtPayContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setEthPrice(tokenPrice: TokenPrice)

        fun setTokens(ethWalletInfo: EthWalletInfo)

        fun sendUsdtSuccess(txid : String)
    }

    interface UsdtPayContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}