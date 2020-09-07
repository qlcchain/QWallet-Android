package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for SwapRecordFragment
 * @Description: $description
 * @date 2020/08/12 15:49:25
 */
interface SwapRecordContract {
    interface View : BaseView<SwapRecordContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setEthGasPrice(string: String)

        fun setEthPrice(tokenPrice: TokenPrice)
    }

    interface SwapRecordContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}