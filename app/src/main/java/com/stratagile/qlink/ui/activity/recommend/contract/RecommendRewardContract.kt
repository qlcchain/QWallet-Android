package com.stratagile.qlink.ui.activity.recommend.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for RecommendRewardActivity
 * @Description: $description
 * @date 2020/01/09 13:57:40
 */
interface RecommendRewardContract {
    interface View : BaseView<RecommendRewardContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface RecommendRewardContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}