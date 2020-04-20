package com.stratagile.qlink.ui.adapter.my

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.data.api.MainAPI
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.mining.MiningIndex
import com.stratagile.qlink.entity.mining.MiningRank
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.view.SmoothCheckBox

class EpidemicOutSideAdapter(array: ArrayList<OutSideBean>) : BaseQuickAdapter<OutSideBean, BaseViewHolder>(R.layout.item_epidemic_out_side, array) {
    override fun convert(helper: BaseViewHolder, item: OutSideBean) {
        if (helper.layoutPosition == 0 || helper.layoutPosition == 7) {
            helper.setVisible(R.id.view1, false)
            helper.setVisible(R.id.view2, false)
        } else {
            helper.setVisible(R.id.view1, true)
            helper.setVisible(R.id.view2, true)
        }
        if (!item.isDone) {
            helper.setBackgroundRes(R.id.view1, R.drawable.bg_dot_bcbcbc)
            helper.setBackgroundRes(R.id.view2, R.drawable.bg_dot_bcbcbc)
            helper.setBackgroundRes(R.id.tvNumber, R.drawable.bg_dot_bcbcbc)
        }
        helper.setText(R.id.tvNumber, item.day.toString())
    }
}