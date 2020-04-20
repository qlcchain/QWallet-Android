package com.stratagile.qlink.ui.activity.my.contract

import com.stratagile.qlink.entity.VCodeVerifyCode
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for EpidemicClaimQlcActivity
 * @Description: $description
 * @date 2020/04/16 15:57:15
 */
interface EpidemicClaimQlcContract {
    interface View : BaseView<EpidemicClaimQlcContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setInviteCode(vCodeVerifyCode: VCodeVerifyCode)
    }

    interface EpidemicClaimQlcContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}