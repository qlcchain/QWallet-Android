package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for EthSwapRecordFragment
 * @Description: $description
 * @date 2020/09/02 13:58:17
 */
interface EthSwapRecordContract {
    interface View : BaseView<EthSwapRecordContractPresenter> {
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

    interface EthSwapRecordContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}