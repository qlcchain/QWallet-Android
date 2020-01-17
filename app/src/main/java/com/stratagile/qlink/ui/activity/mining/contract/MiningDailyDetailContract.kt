package com.stratagile.qlink.ui.activity.mining.contract

import com.stratagile.qlink.entity.mining.MiningRewardList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for MiningDailyDetailActivity
 * @Description: $description
 * @date 2019/11/14 18:13:10
 */
interface MiningDailyDetailContract {
    interface View : BaseView<MiningDailyDetailContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setRewardList(miningRewardList: MiningRewardList, page: Int)
    }

    interface MiningDailyDetailContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}