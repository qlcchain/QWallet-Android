package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.topup.IspList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for SelectRegionActivity
 * @Description: $description
 * @date 2019/12/25 14:50:07
 */
interface SelectRegionContract {
    interface View : BaseView<SelectRegionContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setIsp(ispList: IspList)
    }

    interface SelectRegionContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}