package com.stratagile.qlink.ui.activity.qlc.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for QlcMnemonicbackupActivity
 * @Description: $description
 * @date 2019/06/05 18:37:03
 */
interface QlcMnemonicbackupContract {
    interface View : BaseView<QlcMnemonicbackupContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface QlcMnemonicbackupContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}