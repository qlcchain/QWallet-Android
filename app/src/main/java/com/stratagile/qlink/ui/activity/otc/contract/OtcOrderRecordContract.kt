package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for OtcOrderRecordActivity
 * @Description: $description
 * @date 2019/07/16 17:43:20
 */
interface OtcOrderRecordContract {
    interface View : BaseView<OtcOrderRecordContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface OtcOrderRecordContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}