package com.stratagile.qlink.ui.adapter.stake

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.CurrencyBean
import com.stratagile.qlink.entity.stake.MyStakeList
import com.stratagile.qlink.utils.TimeUtil
import java.math.BigDecimal
import java.math.RoundingMode

class MyStakeAdapter(arrayList : ArrayList<MyStakeList.ResultBean>) : BaseQuickAdapter<MyStakeList.ResultBean, BaseViewHolder>(R.layout.item_my_stake, arrayList) {
    override fun convert(helper: BaseViewHolder, item: MyStakeList.ResultBean) {
        helper.setText(R.id.tvStakeAmount, item.amount.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString())
        when(item.state) {
            "PledgeStart" -> {
                helper.setText(R.id.tvStakeStatus, mContext.getString(R.string.not_successed))
                helper.setTextColor(R.id.tvStakeStatus, mContext.resources.getColor(R.color.color_d0021b))
            }
            "PledgeProcess" -> {
                helper.setText(R.id.tvStakeStatus, mContext.getString(R.string.not_successed))
                helper.setTextColor(R.id.tvStakeStatus, mContext.resources.getColor(R.color.color_d0021b))
                if (item.qgas == 0L) {
                    helper.setText(R.id.tvEarns, "0")
                } else {
                    helper.setText(R.id.tvEarns, item.qgas.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString())
                }
            }

            "PledgeDone" -> {
                helper.setText(R.id.tvStakeStatus, TimeUtil.getOrderTime(item.pledgeTime))
                helper.setTextColor(R.id.tvStakeStatus, mContext.resources.getColor(R.color.color_29282a))
                if (item.qgas == 0L) {
                    helper.setText(R.id.tvEarns, "0")
                } else {
                    helper.setText(R.id.tvEarns, item.qgas.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString())
                }
            }

            "WithdrawStart" -> {

            }
            "WithdrawProcess" -> {
                helper.setText(R.id.tvStakeStatus, mContext.getString(R.string.revoke_failed1))
                helper.setTextColor(R.id.tvStakeStatus, mContext.resources.getColor(R.color.color_d0021b))
                if (item.qgas == 0L) {
                    helper.setText(R.id.tvEarns, "0")
                } else {
                    helper.setText(R.id.tvEarns, item.qgas.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString())
                }
            }
            "WithdrawDone" -> {
                helper.setText(R.id.tvStakeStatus, mContext.getString(R.string.withdrawal_successful))
                helper.setTextColor(R.id.tvStakeStatus, mContext.resources.getColor(R.color.color_0cb8ae))
                if (item.qgas == 0L) {
                    helper.setText(R.id.tvEarns, "0")
                } else {
                    helper.setText(R.id.tvEarns, item.qgas.toBigDecimal().divide(BigDecimal.TEN.pow(8), 8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString())
                }
            }
            else -> {
                helper.setText(R.id.tvEarns, "-/-")
                helper.setText(R.id.tvStakeStatus, item.state)
            }
        }
        when(item.pType) {
            "vote" -> {
                helper.setText(R.id.tvStakeType, mContext.resources.getString(R.string.vote_mining_node))
                helper.setImageResource(R.id.ivAvatar, R.mipmap.mining_node_a)
            }
        }
    }
}