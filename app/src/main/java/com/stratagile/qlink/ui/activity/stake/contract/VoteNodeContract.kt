package com.stratagile.qlink.ui.activity.stake.contract

import com.stratagile.qlink.entity.NeoWalletInfo
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for VoteNodeFragment
 * @Description: $description
 * @date 2019/08/08 16:37:41
 */
interface VoteNodeContract {
    interface View : BaseView<VoteNodeContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setNeoDetail(neoWalletInfo : NeoWalletInfo)
    }

    interface VoteNodeContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}