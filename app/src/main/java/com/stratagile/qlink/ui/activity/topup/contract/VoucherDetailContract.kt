package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for VoucherDetailActivity
 * @Description: $description
 * @date 2019/10/17 17:29:37
 */
interface VoucherDetailContract {
    interface View : BaseView<VoucherDetailContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface VoucherDetailContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}