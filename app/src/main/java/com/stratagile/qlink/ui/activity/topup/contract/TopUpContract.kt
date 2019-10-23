package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.InviteList
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TopUpFragment
 * @Description: $description
 * @date 2019/09/23 15:54:17
 */
interface TopUpContract {
    interface View : BaseView<TopUpContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setProductList(topupProduct: TopupProduct)

        fun setInviteRank(inviteList: InviteList)

        fun setOneFriendReward(dict: Dict)
    }

    interface TopUpContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}