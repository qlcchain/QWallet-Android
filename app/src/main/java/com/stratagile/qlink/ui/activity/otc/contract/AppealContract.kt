package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for AppealFragment
 * @Description: $description
 * @date 2019/07/16 17:53:24
 */
interface AppealContract {
    interface View : BaseView<AppealContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface AppealContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}