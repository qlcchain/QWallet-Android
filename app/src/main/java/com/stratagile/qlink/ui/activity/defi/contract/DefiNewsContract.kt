package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.defi.DefiNewsList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for DefiNewsFragment
 * @Description: $description
 * @date 2020/05/25 17:10:21
 */
interface DefiNewsContract {
    interface View : BaseView<DefiNewsContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setDefiNews(defiNewsList: DefiNewsList, currentPage : Int)
    }

    interface DefiNewsContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}