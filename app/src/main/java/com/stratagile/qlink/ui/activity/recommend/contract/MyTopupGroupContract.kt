package com.stratagile.qlink.ui.activity.recommend.contract

import com.stratagile.qlink.entity.topup.GroupItemList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for MyTopupGroupActivity
 * @Description: $description
 * @date 2020/01/15 16:21:51
 */
interface MyTopupGroupContract {
    interface View : BaseView<MyTopupGroupContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setOrderList(groupItemList: GroupItemList, page : Int)
    }

    interface MyTopupGroupContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}