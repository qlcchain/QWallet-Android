package com.stratagile.qlink.ui.adapter.topup

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.topup.CountryList
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.view.SmoothCheckBox

class MobileAreaAdapter(array: List<CountryList.CountryListBean>) : BaseQuickAdapter<CountryList.CountryListBean, BaseViewHolder>(R.layout.item_mobile_area, array) {
    override fun convert(helper: BaseViewHolder, item: CountryList.CountryListBean) {
        helper.setText(R.id.areaNumber, item.globalRoaming)
        helper.setText(R.id.area, item.nameEn)

    }
}