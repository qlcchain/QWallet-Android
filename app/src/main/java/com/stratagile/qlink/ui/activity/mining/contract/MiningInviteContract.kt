package com.stratagile.qlink.ui.activity.mining.contract

import com.stratagile.qlink.entity.mining.MiningIndex
import com.stratagile.qlink.entity.mining.MiningRank
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for MiningInviteActivity
 * @Description: $description
 * @date 2019/11/14 09:43:06
 */
interface MiningInviteContract {
    interface View : BaseView<MiningInviteContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setRewardRank(miningRank: MiningIndex)
    }

    interface MiningInviteContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}