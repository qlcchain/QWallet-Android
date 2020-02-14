package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TopupSelectDeductionTokenActivity
 * @Description: $description
 * @date 2020/02/13 20:41:09
 */
interface TopupSelectDeductionTokenContract {
    interface View : BaseView<TopupSelectDeductionTokenContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setPayToken(payToken: PayToken)
    }

    interface TopupSelectDeductionTokenContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}