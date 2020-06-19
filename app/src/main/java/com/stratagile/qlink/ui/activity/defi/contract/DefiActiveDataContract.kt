package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.defi.DefiStateList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for DefiActiveDataFragment
 * @Description: $description
 * @date 2020/06/02 10:25:59
 */
interface DefiActiveDataContract {
    interface View : BaseView<DefiActiveDataContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setDefiStateListData(defiStateList: DefiStateList)
    }

    interface DefiActiveDataContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}