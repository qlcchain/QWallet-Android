package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.entity.TokenPrice
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for EthSwapFragment
 * @Description: $description
 * @date 2020/08/15 16:40:33
 */
interface EthSwapContract {
    interface View : BaseView<EthSwapContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setNeoDetail(neoWalletInfo : NeoWalletInfo)

        fun setEthGasPrice(string: String)

        fun setEthPrice(tokenPrice: TokenPrice)
    }

    interface EthSwapContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}