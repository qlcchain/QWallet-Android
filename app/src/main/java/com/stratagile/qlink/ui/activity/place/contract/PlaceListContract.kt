package com.stratagile.qlink.ui.activity.place.contract

import com.stratagile.qlink.entity.ReportList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for PlaceListActivity
 * @Description: $description
 * @date 2020/02/21 21:39:44
 */
interface PlaceListContract {
    interface View : BaseView<PlaceListContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setReportList(reportList: ReportList)
    }

    interface PlaceListContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}