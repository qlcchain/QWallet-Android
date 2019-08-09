package com.stratagile.qlink.ui.activity.stake.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for ConfidantFragment
 * @Description: $description
 * @date 2019/08/08 16:38:01
 */
interface ConfidantContract {
    interface View : BaseView<ConfidantContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface ConfidantContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}