package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
import java.util.ArrayList

/**
 * @author hzp
 * @Package The contract for TradeListFragment
 * @Description: $description
 * @date 2019/08/19 09:38:46
 */
interface TradeListContract {
    interface View : BaseView<TradeListContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        abstract fun setEntrustOrderList(list: ArrayList<EntrustOrderList.OrderListBean>, currentPage: Int)

        abstract fun getEutrustOrderError()
    }

    interface TradeListContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}