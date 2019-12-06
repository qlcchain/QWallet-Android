package com.stratagile.qlink.ui.adapter.topup

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.view.SmoothCheckBox

class MobileAreaAdapter(array: List<Area>) : BaseQuickAdapter<Area, BaseViewHolder>(R.layout.item_mobile_area, array) {
    override fun convert(helper: BaseViewHolder, item: Area) {
        helper.setText(R.id.areaNumber, item.code)
        helper.setText(R.id.area, item.cn)

    }
}