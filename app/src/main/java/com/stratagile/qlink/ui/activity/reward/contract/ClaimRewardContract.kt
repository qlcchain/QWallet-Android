package com.stratagile.qlink.ui.activity.reward.contract

import com.stratagile.qlink.entity.reward.ClaimQgas
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for ClaimRewardActivity
 * @Description: $description
 * @date 2019/10/10 15:28:24
 */
interface ClaimRewardContract {
    interface View : BaseView<ClaimRewardContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun claimQgasBack(claimQgas: ClaimQgas)
    }

    interface ClaimRewardContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}