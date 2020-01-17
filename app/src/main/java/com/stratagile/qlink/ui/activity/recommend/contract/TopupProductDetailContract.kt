package com.stratagile.qlink.ui.activity.recommend.contract

import com.stratagile.qlink.entity.topup.*
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TopupProductDetailActivity
 * @Description: $description
 * @date 2020/01/13 15:36:22
 */
interface TopupProductDetailContract {
    interface View : BaseView<TopupProductDetailContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setGroupKindList(topupGroupKindList: TopupGroupKindList)
        fun showProxyDialog()

        fun showStakeDialog()


        fun createGroupBack(createGroup: CreateGroup)

        fun setGroupList(topupGroupList: TopupGroupList)

        fun joinGroupBack(topupJoinGroup: TopupJoinGroup)

        fun createTopupOrderError()

        fun createTopupOrderSuccess(topupOrder: TopupOrder)
    }

    interface TopupProductDetailContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}