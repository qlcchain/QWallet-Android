package com.stratagile.qlink.ui.activity.my.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for EpidemicInviteNowActivity
 * @Description: $description
 * @date 2020/04/17 10:25:30
 */
interface EpidemicInviteNowContract {
    interface View : BaseView<EpidemicInviteNowContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface EpidemicInviteNowContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}