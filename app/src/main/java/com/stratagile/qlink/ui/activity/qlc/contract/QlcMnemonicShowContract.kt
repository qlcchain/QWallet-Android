package com.stratagile.qlink.ui.activity.qlc.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for QlcMnemonicShowActivity
 * @Description: $description
 * @date 2019/06/05 18:36:43
 */
interface QlcMnemonicShowContract {
    interface View : BaseView<QlcMnemonicShowContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface QlcMnemonicShowContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}