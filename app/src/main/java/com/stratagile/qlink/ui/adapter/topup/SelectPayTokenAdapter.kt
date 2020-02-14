package com.stratagile.qlink.ui.adapter.topup

import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.pawegio.kandroid.setWidth
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.utils.UIUtils
import com.stratagile.qlink.view.SmoothCheckBox

class SelectPayTokenAdapter(array: ArrayList<PayToken.PayTokenListBean>) : BaseQuickAdapter<PayToken.PayTokenListBean, BaseViewHolder>(R.layout.item_select_pay_token, array) {
    override fun convert(helper: BaseViewHolder, item: PayToken.PayTokenListBean) {
        var llParent = helper.getView<RelativeLayout>(R.id.llParent)
        helper.setText(R.id.tvToken, item.symbol)
        if ("".equals(item.logo_png)) {
            Glide.with(mContext)
                    .load(mContext.resources.getIdentifier(item.symbol.toLowerCase(), "mipmap", mContext.packageName))
                    .into(helper.getView(R.id.ivToken))
        } else {
            Glide.with(mContext)
                    .load(item.logo_png)
                    .into(helper.getView(R.id.ivToken))
        }
    }
}