package com.stratagile.qlink.ui.activity.reward.contract

import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.reward.RewardList
import com.stratagile.qlink.entity.reward.RewardTotal
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for MyClaimActivity
 * @Description: $description
 * @date 2019/10/09 11:57:31
 */
interface MyClaimContract {
    interface View : BaseView<MyClaimContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setRewardQlcAmount(dict: Dict)

        fun setCanClaimTotal(rewardTotal: RewardTotal)

        fun setClaimedTotal(rewardTotal: RewardTotal)

        fun setRewardList(rewardList: RewardList, page : Int)

    }

    interface MyClaimContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}