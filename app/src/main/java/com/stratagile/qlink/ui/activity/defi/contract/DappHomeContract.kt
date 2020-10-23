package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for DappHomeFragment
 * @Description: $description
 * @date 2020/10/15 16:00:43
 */
interface DappHomeContract {
    interface View : BaseView<DappHomeContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface DappHomeContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}