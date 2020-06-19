package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.defi.DefiNewsDetail
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for DefiNewsDetailActivity
 * @Description: $description
 * @date 2020/06/05 14:09:12
 */
interface DefiNewsDetailContract {
    interface View : BaseView<DefiNewsDetailContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setNewsDetail(defiNewsDetail: DefiNewsDetail)
    }

    interface DefiNewsDetailContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}