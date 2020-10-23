package com.stratagile.qlink.ui.activity.topup.contract

import com.stratagile.qlink.entity.*
import com.stratagile.qlink.entity.defi.DefiList
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.topup.*
import com.stratagile.qlink.ui.activity.base.BasePresenter
import com.stratagile.qlink.ui.activity.base.BaseView
/**
 * @author hzp
 * @Package The contract for TopUpFragment
 * @Description: $description
 * @date 2019/09/23 15:54:17
 */
interface TopUpContract {
    interface View : BaseView<TopUpContractPresenter> {
        /**
         *
         */
        fun showProgressDialog()

        /**
         *
         */
        fun closeProgressDialog()

        fun setOneFriendReward(dict: Dict)
        fun setProxyActivityBanner(dict: Dict)

        fun setChartData(data: KLine)

        fun setQlcPrice(tokenPrice: TokenPrice)

        fun setPayToken(payToken: PayToken)

        fun setBurnQgasAct(burnQgasAct: BurnQgasAct)

        fun setIndexInterface (indexInterface: IndexInterface)

        fun setLocation(location: Location)

        fun setDefiList(defiList: DefiList)
    }

    interface TopUpContractPresenter : BasePresenter {
//        /**
//         *
//         */
//        fun getBusinessInfo(map : Map)
    }
}