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
import com.today.step.net.EpidemicList

class EpidemicRedPaperAdapter(array: ArrayList<EpidemicList.RecordListBean>) : BaseQuickAdapter<EpidemicList.RecordListBean, BaseViewHolder>(R.layout.item_epidemic_red_paper, array) {
    override fun convert(helper: BaseViewHolder, item: EpidemicList.RecordListBean) {
        helper.setText(R.id.tvContent, mContext.getString(R.string.claim_daily_qgas_bounty_simply_by_checking_out_the_covid_19_live_updates_page_for_1_consecutive_day, item.days.toString(), item.qlcAmount.toBigDecimal().stripTrailingZeros().toPlainString()))
    }
}