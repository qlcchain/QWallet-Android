package com.stratagile.qlink.ui.activity.stake.contract

import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TokenMintageFragment
 * @Description: $description
 * @date 2019/08/08 16:38:20
 */
interface TokenMintageContract {
    interface View : BaseView<TokenMintageContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setNeoDetail(neoWalletInfo: NeoWalletInfo)
    }

    interface TokenMintageContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}