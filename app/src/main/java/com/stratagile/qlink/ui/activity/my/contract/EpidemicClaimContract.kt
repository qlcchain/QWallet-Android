package com.stratagile.qlink.ui.activity.my.contract

import com.stratagile.qlink.entity.VCodeVerifyCode
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for EpidemicClaimActivity
 * @Description: $description
 * @date 2020/04/15 17:22:27
 */
interface EpidemicClaimContract {
    interface View : BaseView<EpidemicClaimContractPresenter> {
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

    interface EpidemicClaimContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}