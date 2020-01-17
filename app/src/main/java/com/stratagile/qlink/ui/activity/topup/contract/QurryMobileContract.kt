package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.topup.*
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for QurryMobileActivity
 * @Description: $description
 * @date 2019/09/24 14:50:33
 */
interface QurryMobileContract {
    interface View : BaseView<QurryMobileContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setProductList(topupProduct: TopupProduct)

        fun setPayTokenAdapter(payToken: PayToken)

        fun createTopupOrderError()

        fun createTopupOrderSuccess(topupOrder: TopupOrder)

        fun setIsp(ispList: IspList)

        fun setProvinceList(ispList: IspList)

        fun setGroupDate(dict: Dict, position : Int)

        fun setGroupKindList(topupGroupKindList: TopupGroupKindList)
    }

    interface QurryMobileContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}