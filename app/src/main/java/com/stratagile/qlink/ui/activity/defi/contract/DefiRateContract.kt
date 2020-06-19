package com.stratagile.qlink.ui.activity.defi.contract

import com.stratagile.qlink.entity.defi.DefiRating
import com.stratagile.qlink.entity.defi.RatingInfo
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for DefiRateActivity
 * @Description: $description
 * @date 2020/06/03 14:17:18
 */
interface DefiRateContract {
    interface View : BaseView<DefiRateContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setRatingInfo(ratingInfo: RatingInfo)

        fun setNeoQLcAmount(amount : Double, over : Boolean)

        fun setNeoQlcAmount(amount : Double, address : String)

        fun setQLCQLCAmount(amount: Double, over: Boolean)
        fun defiRatingBack(defiRating: DefiRating)

        fun ratingError()
    }

    interface DefiRateContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}