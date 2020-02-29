package com.stratagile.qlink.ui.activity.my.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for BurnIntroduceActivity
 * @Description: $description
 * @date 2020/02/29 17:33:46
 */
interface BurnIntroduceContract {
    interface View : BaseView<BurnIntroduceContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface BurnIntroduceContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}