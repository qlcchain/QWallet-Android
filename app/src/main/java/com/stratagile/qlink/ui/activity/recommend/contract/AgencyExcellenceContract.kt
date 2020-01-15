package com.stratagile.qlink.ui.activity.recommend.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for AgencyExcellenceActivity
 * @Description: $description
 * @date 2020/01/09 13:58:26
 */
interface AgencyExcellenceContract {
    interface View : BaseView<AgencyExcellenceContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface AgencyExcellenceContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}