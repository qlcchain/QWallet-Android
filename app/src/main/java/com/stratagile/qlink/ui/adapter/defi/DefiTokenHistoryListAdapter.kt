package com.stratagile.qlink.ui.adapter.defi

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.defi.DefiList
import com.stratagile.qlink.entity.defi.DefiStateList
import com.stratagile.qlink.utils.DefiUtil
import com.stratagile.qlink.utils.TimeUtil
import com.stratagile.qlink.utils.UIUtils

class DefiTokenHistoryListAdapter(array: ArrayList<DefiStateList.HistoricalStatsListBean>) : BaseQuickAdapter<DefiStateList.HistoricalStatsListBean, BaseViewHolder>(R.layout.item_defi_token_history_list, array) {
    override fun convert(helper: BaseViewHolder, item: DefiStateList.HistoricalStatsListBean) {
        helper.setText(R.id.tvTime, TimeUtil.getStakeTime(TimeUtil.timeStamp(item.statsDate) / 1000))
        helper.setText(R.id.tvTvl, DefiUtil.parseUsd(item.tvlUsd.toString()))
        if (item.usdPoor > 0) {
            helper.setText(R.id.tvTvlRange, "+" + DefiUtil.parseValue(item.usdPoor.toString(), false))
            helper.setTextColor(R.id.tvTvlRange, mContext.resources.getColor(R.color.color_07cdb3))
        } else {
            helper.setText(R.id.tvTvlRange, DefiUtil.parseValue(item.usdPoor.toString(), false))
            helper.setTextColor(R.id.tvTvlRange, mContext.resources.getColor(R.color.color_f50f60))
        }

        if (item.tvlEth > 0) {
            helper.setVisible(R.id.llEth, true)
            helper.setText(R.id.tvEth, DefiUtil.parseValue(item.tvlEth.toString()) + " ETH")
            if (item.ethPoor > 0) {
                helper.setText(R.id.tvEthRange, "+" + DefiUtil.parseValue(item.ethPoor.toString(), false) + " ETH")
                helper.setTextColor(R.id.tvEthRange, mContext.resources.getColor(R.color.color_07cdb3))
            } else {
                helper.setText(R.id.tvEthRange, DefiUtil.parseValue(item.ethPoor.toString(), false) + " ETH")
                helper.setTextColor(R.id.tvEthRange, mContext.resources.getColor(R.color.color_f50f60))
            }
        } else {
            helper.setVisible(R.id.llEth, true)
            helper.setText(R.id.tvEth, "0 ETH")
            helper.setText(R.id.tvEthRange, "0 ETH")
            helper.setTextColor(R.id.tvEthRange, mContext.resources.getColor(R.color.color_07cdb3))
        }

    }
}