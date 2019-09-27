package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.topup.TopupProduct
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
    }

    interface QurryMobileContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}