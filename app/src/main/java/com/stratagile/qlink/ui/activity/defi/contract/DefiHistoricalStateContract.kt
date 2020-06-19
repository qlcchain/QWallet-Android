package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.defi.DefiStateList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for DefiHistoricalStateFragment
 * @Description: $description
 * @date 2020/06/02 10:26:24
 */
interface DefiHistoricalStateContract {
    interface View : BaseView<DefiHistoricalStateContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setDefiStateListData(defiStateList: DefiStateList, currentPage : Int)
    }

    interface DefiHistoricalStateContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}