package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.otc.TradePair
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
import java.util.ArrayList

/**
 * @author hzp
 * @Package The contract for SelectCurrencyActivity
 * @Description: $description
 * @date 2019/08/19 15:22:57
 */
interface SelectCurrencyContract {
    interface View : BaseView<SelectCurrencyContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        abstract fun setRemoteTradePairs(pairsListBeans: ArrayList<TradePair.PairsListBean>)
    }

    interface SelectCurrencyContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}