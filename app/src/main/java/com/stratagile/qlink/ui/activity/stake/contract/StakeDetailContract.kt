package com.stratagile.qlink.ui.activity.stake.contract

import com.stratagile.qlink.entity.stake.UnLock
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for StakeDetailActivity
 * @Description: $description
 * @date 2019/08/09 15:26:02
 */
interface StakeDetailContract {
    interface View : BaseView<StakeDetailContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun sign(unLock: UnLock)
    }

    interface StakeDetailContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}