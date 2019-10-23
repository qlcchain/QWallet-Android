package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlc.entity.QlcTokenbalance
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for OtcQlcChainPayActivity
 * @Description: $description
 * @date 2019/08/23 12:01:58
 */
interface OtcQlcChainPayContract {
    interface View : BaseView<OtcQlcChainPayContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setQlcChainToken(arrayList: ArrayList<QlcTokenbalance>)

        fun sendTokenSuccess(txid : String)

        fun sendTokenFailed()
    }

    interface OtcQlcChainPayContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}