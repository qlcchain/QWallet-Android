package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for CloasedOrderFragment
 * @Description: $description
 * @date 2019/07/17 14:39:46
 */
interface CloasedOrderContract {
    interface View : BaseView<CloasedOrderContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface CloasedOrderContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}