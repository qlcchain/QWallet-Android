package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.defi.ServerEthPrice
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for ConfirmActivity
 * @Description: $description
 * @date 2020/10/14 16:59:28
 */
interface ConfirmContract {
    interface View : BaseView<ConfirmContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setEthGasPrice(serverEthPrice: ServerEthPrice)
    }

    interface ConfirmContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)

    }
}