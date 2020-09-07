package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TransformQlcActivity
 * @Description: $description
 * @date 2020/08/12 15:15:34
 */
interface TransformQlcContract {
    interface View : BaseView<TransformQlcContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface TransformQlcContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}