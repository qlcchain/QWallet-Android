package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.topup.TopupJoinGroup
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TopupDeductionQlcChainActivity
 * @Description: $description
 * @date 2019/12/26 14:45:12
 */
interface TopupDeductionQlcChainContract {
    interface View : BaseView<TopupDeductionQlcChainContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun saveDeductionTokenTxidBack(topupOrder: TopupOrder)

        fun saveItemDeductionTokenTxidBack(topupJoinGroup: TopupJoinGroup)

        fun itemOrderStatus(topupJoinGroup: TopupJoinGroup)

        fun topupOrderStatus(topupOrder: TopupOrder)
        fun createTopupOrderError()

        fun createTopupOrderSuccess(topupOrder: TopupOrder)

        fun setMainAddress()
    }

    interface TopupDeductionQlcChainContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}