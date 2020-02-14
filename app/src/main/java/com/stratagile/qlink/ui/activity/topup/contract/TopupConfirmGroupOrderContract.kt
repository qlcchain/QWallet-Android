package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.topup.TopupJoinGroup
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TopupConfirmGroupOrderActivity
 * @Description: $description
 * @date 2020/02/13 16:15:05
 */
interface TopupConfirmGroupOrderContract {
    interface View : BaseView<TopupConfirmGroupOrderContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun joinGroupBack(topupJoinGroup: TopupJoinGroup)

        fun createTopupOrderSuccess(topupOrder: TopupOrder)
    }

    interface TopupConfirmGroupOrderContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}