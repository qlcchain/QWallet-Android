package com.stratagile.qlink.ui.adapter.reward

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.CurrencyBean
import com.stratagile.qlink.entity.reward.RewardList
import com.stratagile.qlink.entity.stake.MyStakeList
import com.stratagile.qlink.utils.TimeUtil
import java.math.BigDecimal
import java.math.RoundingMode

class RewardListAdapter(arrayList : ArrayList<RewardList.RewardListBean>) : BaseQuickAdapter<RewardList.RewardListBean, BaseViewHolder>(R.layout.item_reward_list, arrayList) {
    override fun convert(helper: BaseViewHolder, item: RewardList.RewardListBean) {
        if (helper.layoutPosition == 0) {
            helper.setVisible(R.id.viewLine, false)
        } else {
            helper.setVisible(R.id.viewLine, true)
        }
        helper.setText(R.id.tvTime, item.rewardDate)
        helper.setText(R.id.tvRewardEarn, "+" + item.rewardAmount + " QGAS")
    }
}