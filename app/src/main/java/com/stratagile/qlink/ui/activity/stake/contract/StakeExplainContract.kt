package com.stratagile.qlink.ui.activity.stake.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for StakeExplainActivity
 * @Description: $description
 * @date 2019/08/09 15:25:43
 */
interface StakeExplainContract {
    interface View : BaseView<StakeExplainContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface StakeExplainContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}