package com.stratagile.qlink.ui.activity.mining.contract

import com.stratagile.qlink.entity.reward.ClaimQgas
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for MiningRewardActivity
 * @Description: $description
 * @date 2019/11/15 15:49:47
 */
interface MiningRewardContract {
    interface View : BaseView<MiningRewardContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun claimQlcBack(claimQgas: ClaimQgas)
    }

    interface MiningRewardContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}