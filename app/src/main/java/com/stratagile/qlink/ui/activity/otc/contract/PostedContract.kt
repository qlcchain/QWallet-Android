package com.stratagile.qlink.ui.activity.otc.contract

import com.stratagile.qlink.entity.EntrustOrderList
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
import java.util.ArrayList

/**
 * @author hzp
 * @Package The contract for PostedFragment
 * @Description: $description
 * @date 2019/07/16 17:52:28
 */
interface PostedContract {
    interface View : BaseView<PostedContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setEntrustOrderList(list: ArrayList<EntrustOrderList.OrderListBean>)
    }

    interface PostedContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}