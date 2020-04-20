package com.stratagile.qlink.ui.activity.my.contract

import com.stratagile.qlink.entity.Location
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for EpidemicRedPaperActivity
 * @Description: $description
 * @date 2020/04/13 17:05:33
 */
interface EpidemicRedPaperContract {
    interface View : BaseView<EpidemicRedPaperContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setLocation(location: Location)
    }

    interface EpidemicRedPaperContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}