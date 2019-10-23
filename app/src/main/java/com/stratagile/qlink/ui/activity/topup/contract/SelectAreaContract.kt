package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for SelectAreaActivity
 * @Description: $description
 * @date 2019/09/24 16:07:07
 */
interface SelectAreaContract {
    interface View : BaseView<SelectAreaContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface SelectAreaContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}