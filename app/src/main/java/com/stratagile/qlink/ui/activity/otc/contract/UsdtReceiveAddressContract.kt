package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for UsdtReceiveAddressActivity
 * @Description: $description
 * @date 2019/07/17 16:50:34
 */
interface UsdtReceiveAddressContract {
    interface View : BaseView<UsdtReceiveAddressContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface UsdtReceiveAddressContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}