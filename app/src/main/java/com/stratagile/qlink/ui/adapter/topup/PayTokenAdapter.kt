package com.stratagile.qlink.ui.adapter.topup

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.view.SmoothCheckBox

class PayTokenAdapter(array: ArrayList<PayToken.PayTokenListBean>) : BaseQuickAdapter<PayToken.PayTokenListBean, BaseViewHolder>(R.layout.item_pay_token, array) {
    override fun convert(helper: BaseViewHolder, item: PayToken.PayTokenListBean) {
        if (item.isSelected) {
            Glide.with(mContext)
                    .load(mContext.resources.getIdentifier(item.symbol.toLowerCase(), "mipmap", mContext.packageName))
                    .into(helper.getView(R.id.tokenIcon))
            helper.setText(R.id.tokenName, item.symbol)
            helper.setBackgroundRes(R.id.llParent, R.drawable.bg_white_stroke_maincolor_6dp)
            helper.setGone(R.id.ivSelected, true)
            helper.setTextColor(R.id.tokenName, mContext.resources.getColor(R.color.mainColor))
            helper.getView<ImageView>(R.id.tokenIcon).imageAlpha = 0xFF
        } else {
            Glide.with(mContext)
                    .load(mContext.resources.getIdentifier(item.symbol.toLowerCase(), "mipmap", mContext.packageName))
                    .into(helper.getView(R.id.tokenIcon))
            helper.setText(R.id.tokenName, item.symbol)
            helper.getView<ImageView>(R.id.tokenIcon).imageAlpha = 0x80
            helper.setBackgroundRes(R.id.llParent, R.drawable.bg_white_stroke_e3e3e3_6dp)
            helper.setGone(R.id.ivSelected, false)
            helper.setTextColor(R.id.tokenName, mContext.resources.getColor(R.color.color_1e1e24_50))
        }
    }
}