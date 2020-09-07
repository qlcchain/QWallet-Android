package com.stratagile.qlink.ui.activity.stake.contract

import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.reward.RewardTotal
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for MyStakeActivity
 * @Description: $description
 * @date 2019/08/08 15:32:14
 */
interface MyStakeContract {
    interface View : BaseView<MyStakeContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setRewardQlcAmount(dict: Dict)

        fun setClaimedTotal(rewardTotal: RewardTotal)

        fun setQLCQLCAmount(amount : Long)
    }

    interface MyStakeContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}