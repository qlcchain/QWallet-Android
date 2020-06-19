package com.stratagile.qlink.ui.adapter.defi

import android.util.Log
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.defi.DefiCategory
import com.stratagile.qlink.entity.defi.DefiJson
import com.stratagile.qlink.entity.defi.KeyStateBean
import com.stratagile.qlink.utils.DefiUtil
import com.stratagile.qlink.view.SmoothCheckBox

class DefiKeyStateAdapter(array: ArrayList<KeyStateBean>) : BaseQuickAdapter<KeyStateBean, BaseViewHolder>(R.layout.item_defi_key_state, array) {
    override fun convert(helper: BaseViewHolder, item: KeyStateBean) {
        helper.setText(R.id.tvName, "in " + item.name)
        if (item.name.equals("usdt", true)) {
            helper.setText(R.id.tvCount, DefiUtil.parseValue(item.value.toString(), true))
        } else {
            helper.setText(R.id.tvCount, DefiUtil.parseValue(item.value.toString()) + " " + item.name)
        }
        if (item.relative_1d > 0) {
            helper.setTextColor(R.id.tvRange, mContext.resources.getColor(R.color.color_07cdb3))
            helper.setText(R.id.tvRange, "+" + item.relative_1d.toString() + "%")
        } else {
            helper.setTextColor(R.id.tvRange, mContext.resources.getColor(R.color.color_ff3669))
            helper.setText(R.id.tvRange, "" + item.relative_1d.toString() + "%")
        }
    }
}