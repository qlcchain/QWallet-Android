package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.DefiPrice
import com.stratagile.qlink.entity.defi.DefiCategory
import com.stratagile.qlink.entity.defi.DefiList
import com.stratagile.qlink.entity.defi.DefiStateList
import com.stratagile.qlink.entity.defi.DefiStatsCache
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for DefiListFragment
 * @Description: $description
 * @date 2020/05/25 17:10:05
 */
interface DefiListContract {
    interface View : BaseView<DefiListContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setCategoryList(category: DefiCategory)

        fun setDefiList(defiList: DefiList, currentPage : Int)

        fun setDefiStats(defiStateCache: DefiStatsCache)

        fun setDefiPrice(defiPrice : DefiPrice)
    }

    interface DefiListContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}