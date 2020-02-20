package com.stratagile.qlink.ui.activity.place.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for SmsVourchActivity
 * @Description: $description
 * @date 2020/02/20 22:53:22
 */
interface SmsVourchContract {
    interface View : BaseView<SmsVourchContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface SmsVourchContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}