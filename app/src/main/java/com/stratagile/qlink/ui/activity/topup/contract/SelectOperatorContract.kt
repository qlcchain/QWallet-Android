package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.topup.IspList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for SelectOperatorActivity
 * @Description: $description
 * @date 2019/12/25 10:21:32
 */
interface SelectOperatorContract {
    interface View : BaseView<SelectOperatorContractPresenter> {
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

    interface SelectOperatorContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}