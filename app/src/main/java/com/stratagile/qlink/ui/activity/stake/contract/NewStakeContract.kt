package com.stratagile.qlink.ui.activity.stake.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for NewStakeActivity
 * @Description: $description
 * @date 2019/08/08 16:33:44
 */
interface NewStakeContract {
    interface View : BaseView<NewStakeContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface NewStakeContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}