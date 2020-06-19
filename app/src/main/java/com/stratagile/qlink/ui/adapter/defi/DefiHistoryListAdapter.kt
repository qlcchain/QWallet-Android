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
import com.stratagile.qlink.utils.DefiUtil
import com.stratagile.qlink.utils.UIUtils

class DefiHistoryListAdapter(array: ArrayList<DefiList.ProjectListBean>) : BaseQuickAdapter<DefiList.ProjectListBean, BaseViewHolder>(R.layout.item_defi_history_list, array) {
    override fun convert(helper: BaseViewHolder, item: DefiList.ProjectListBean) {
        val imageView = helper.getView<ImageView>(R.id.ivAvatar)
        if ("".equals(item.shortName)) {
            helper.setText(R.id.tvDefiProjectName, item.name)
            Glide.with(mContext)
                    .load(mContext.resources.getIdentifier(item.name.toLowerCase().replace(" ", "_").replace("-", "_"), "mipmap", mContext.packageName))
                    .into(imageView)
        } else {
            helper.setText(R.id.tvDefiProjectName, item.shortName)
            Glide.with(mContext)
                    .load(mContext.resources.getIdentifier(item.shortName.toLowerCase().replace(" ", "_").replace("-", "_"), "mipmap", mContext.packageName))
                    .into(imageView)
        }
    }
}