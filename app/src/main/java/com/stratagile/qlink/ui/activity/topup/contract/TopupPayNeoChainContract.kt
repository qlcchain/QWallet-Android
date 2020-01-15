package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.entity.topup.TopupJoinGroup
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TopupPayNeoChainActivity
 * @Description: $description
 * @date 2019/12/26 16:34:35
 */
interface TopupPayNeoChainContract {
    interface View : BaseView<TopupPayNeoChainContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setNeoDetail(neoWalletInfo: NeoWalletInfo)

        fun savePayTokenTxidBack(topupOrder: TopupOrder)

        fun saveItemPayTokenTxidBack(topupJoinGroup: TopupJoinGroup)
        fun saveItemPayTokenError()

        fun savePayTokenTxidError()
    }

    interface TopupPayNeoChainContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}