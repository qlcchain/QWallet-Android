package com.stratagile.qlink.ui.adapter.topup

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.view.SmoothCheckBox

class MobileAreaAdapter(array: ArrayList<Area.AreaBean>) : BaseQuickAdapter<Area.AreaBean, BaseViewHolder>(R.layout.item_mobile_area, array) {
    override fun convert(helper: BaseViewHolder, item: Area.AreaBean) {
        helper.setText(R.id.areaNumber, "+" + item.number)
        helper.setText(R.id.area, item.country)

    }
}