package com.stratagile.qlink.ui.activity.my.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for EpidemicWebViewActivity
 * @Description: $description
 * @date 2020/04/16 17:48:35
 */
interface EpidemicWebViewContract {
    interface View : BaseView<EpidemicWebViewContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface EpidemicWebViewContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}