package com.stratagile.qlink.ui.adapter.mining

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.CurrencyBean
import com.stratagile.qlink.entity.mining.MiningRewardList
import com.stratagile.qlink.entity.stake.MyStakeList
import com.stratagile.qlink.utils.TimeUtil
import java.math.BigDecimal
import java.math.RoundingMode

class MiningRewardListAdapter(arrayList : ArrayList<MiningRewardList.ListBean>) : BaseQuickAdapter<MiningRewardList.ListBean, BaseViewHolder>(R.layout.item_mining_reward_list, arrayList) {
    override fun convert(helper: BaseViewHolder, item: MiningRewardList.ListBean) {
        if (helper.layoutPosition == 0) {
            helper.setVisible(R.id.viewLine, false)
        } else {
            helper.setVisible(R.id.viewLine, true)
        }
        helper.setText(R.id.tvTime, TimeUtil.timeConvert(item.rewardDate))
        helper.setText(R.id.tvRewardEarn, "+" + item.rewardAmount + " QLC")
    }
}