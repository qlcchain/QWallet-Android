package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for SearchDefiActivity
 * @Description: $description
 * @date 2020/10/22 15:09:25
 */
interface SearchDefiContract {
    interface View : BaseView<SearchDefiContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface SearchDefiContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}