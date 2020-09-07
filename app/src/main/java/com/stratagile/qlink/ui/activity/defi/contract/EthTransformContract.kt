package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for EthTransformActivity
 * @Description: $description
 * @date 2020/08/15 16:13:43
 */
interface EthTransformContract {
    interface View : BaseView<EthTransformContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface EthTransformContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}