package com.stratagile.qlink.ui.adapter.defi

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.db.SwapRecord
import com.stratagile.qlink.entity.defi.DefiList
import com.stratagile.qlink.entity.defi.DexEntity
import com.stratagile.qlink.utils.TimeUtil
import kotlinx.android.synthetic.main.activity_swap_detail.*

class DexListAdapter(array: MutableList<DefiList.ProjectListBean>) : BaseQuickAdapter<DefiList.ProjectListBean, BaseViewHolder>(R.layout.item_dex_list, array) {
    override fun convert(helper: BaseViewHolder, item: DefiList.ProjectListBean) {
        helper.setText(R.id.tvUrl, item.swapUrl)
        val imageView = helper.getView<ImageView>(R.id.ivAvatar)
        if ("".equals(item.shortName)) {
            var resource = mContext.resources.getIdentifier(item.name.toLowerCase().replace(" ", "_").replace("-", "_"), "mipmap", mContext.packageName)
            helper.setText(R.id.tvName, item.name)
            if (resource == 0) {
                KLog.i(item.logo)
                Glide.with(mContext)
                        .load(AppConfig.instance.baseUrl + item.logo)
                        .into(imageView)
            } else {
                Glide.with(mContext)
                        .load(mContext.resources.getIdentifier(item.name.toLowerCase().replace(" ", "_").replace("-", "_"), "mipmap", mContext.packageName))
                        .into(imageView)
            }
        } else {
            helper.setText(R.id.tvName, item.shortName)
            var resource = mContext.resources.getIdentifier(item.shortName.toLowerCase().replace(" ", "_").replace("-", "_"), "mipmap", mContext.packageName)
            KLog.i(resource)
            if (resource == 0) {
                Glide.with(mContext)
                        .load(AppConfig.instance.baseUrl + item.logo)
                        .into(imageView)
            } else {
                Glide.with(mContext)
                        .load(mContext.resources.getIdentifier(item.shortName.toLowerCase().replace(" ", "_").replace("-", "_"), "mipmap", mContext.packageName))
                        .into(imageView)
            }
        }
    }
}