package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for SwapFragment
 * @Description: $description
 * @date 2020/08/12 15:49:07
 */
interface SwapContract {
    interface View : BaseView<SwapContractPresenter> {
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

    interface SwapContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}