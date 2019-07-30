package com.stratagile.qlink.ui.activity.qlc.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for ImportQlcMnemonicFragment
 * @Description: $description
 * @date 2019/06/06 10:37:31
 */
interface ImportQlcMnemonicContract {
    interface View : BaseView<ImportQlcMnemonicContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface ImportQlcMnemonicContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}