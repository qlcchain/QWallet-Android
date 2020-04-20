package com.stratagile.qlink.ui.activity.my.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for CumulativeQgasClaimedActivity
 * @Description: $description
 * @date 2020/04/15 09:37:37
 */
interface CumulativeQgasClaimedContract {
    interface View : BaseView<CumulativeQgasClaimedContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface CumulativeQgasClaimedContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}