package com.stratagile.qlink.ui.activity.neo.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for NeoTestActivity
 * @Description: $description
 * @date 2019/06/10 10:30:03
 */
interface NeoTestContract {
    interface View : BaseView<NeoTestContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface NeoTestContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}