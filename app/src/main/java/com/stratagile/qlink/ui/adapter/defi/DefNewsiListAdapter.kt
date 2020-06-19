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
import com.stratagile.qlink.entity.defi.DefiNewsList
import com.stratagile.qlink.utils.DefiUtil
import com.stratagile.qlink.utils.UIUtils

class DefNewsiListAdapter(array: ArrayList<DefiNewsList.NewsListBean>) : BaseQuickAdapter<DefiNewsList.NewsListBean, BaseViewHolder>(R.layout.item_defi_news_list, array) {
    override fun convert(helper: BaseViewHolder, item: DefiNewsList.NewsListBean) {
        helper.setText(R.id.tvTitle, item.title)
        helper.setText(R.id.tvContent, item.leadText)
        helper.setText(R.id.tvTime, item.createDate.substring(0, 10))
    }
}