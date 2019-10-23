package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for OtcNeoChainPayActivity
 * @Description: $description
 * @date 2019/08/21 15:07:05
 */
interface OtcNeoChainPayContract {
    interface View : BaseView<OtcNeoChainPayContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setNeoDetail(neoWalletInfo : NeoWalletInfo)

        fun sendUsdtSuccess(txid : String)
    }

    interface OtcNeoChainPayContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)

    }
}