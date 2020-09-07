package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.defi.DefiDetail
import com.stratagile.qlink.entity.defi.DefiStateList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for DefiDetailActivity
 * @Description: $description
 * @date 2020/05/29 09:13:19
 */
interface DefiDetailContract {
    interface View : BaseView<DefiDetailContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setDefiDetail(defiDetail: DefiDetail)

        fun setDefiStateList(defiStateList: DefiStateList)

        fun setDetailError(error : String?)
    }

    interface DefiDetailContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}