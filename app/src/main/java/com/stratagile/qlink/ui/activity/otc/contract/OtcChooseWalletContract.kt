package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for OtcChooseWalletActivity
 * @Description: $description
 * @date 2019/07/29 11:54:14
 */
interface OtcChooseWalletContract {
    interface View : BaseView<OtcChooseWalletContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface OtcChooseWalletContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}