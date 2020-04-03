package com.stratagile.qlink.ui.activity.place.contract

import com.stratagile.qlink.entity.SmsReport
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for PlaceVisitActivity
 * @Description: $description
 * @date 2020/02/20 10:07:00
 */
interface PlaceVisitContract {
    interface View : BaseView<PlaceVisitContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun reportBack(smsReport: SmsReport)
    }

    interface PlaceVisitContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}