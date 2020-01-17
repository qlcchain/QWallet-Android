package com.stratagile.qlink.ui.activity.recommend.contract

import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for GroupExplainActivity
 * @Description: $description
 * @date 2020/01/17 13:37:58
 */
interface GroupExplainContract {
    interface View : BaseView<GroupExplainContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()
    }

    interface GroupExplainContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}