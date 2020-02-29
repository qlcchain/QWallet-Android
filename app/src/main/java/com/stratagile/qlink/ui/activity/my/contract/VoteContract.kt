package com.stratagile.qlink.ui.activity.my.contract

import com.stratagile.qlink.entity.VoteResult
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for VoteActivity
 * @Description: $description
 * @date 2020/02/26 10:34:02
 */
interface VoteContract {
    interface View : BaseView<VoteContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun voteBack()

        fun setAppDict(dict: Dict)

        fun setVoteResult(voteResult: VoteResult)
    }

    interface VoteContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}