package com.stratagile.qlink.ui.activity.recommend.contract

import com.stratagile.qlink.entity.UserInfo
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for OpenAgentActivity
 * @Description: $description
 * @date 2020/01/09 13:59:03
 */
interface OpenAgentContract {
    interface View : BaseView<OpenAgentContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun bindAddressSuccess()

        fun setUsrInfo(userInfo: UserInfo)
    }

    interface OpenAgentContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}