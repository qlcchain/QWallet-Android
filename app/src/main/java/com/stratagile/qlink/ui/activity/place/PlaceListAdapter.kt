package com.stratagile.qlink.ui.activity.place

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.SmsReport
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.view.SmoothCheckBox

class PlaceListAdapter(array: ArrayList<SmsReport.ReportBean>) : BaseQuickAdapter<SmsReport.ReportBean, BaseViewHolder>(R.layout.item_place_list, array) {
    var phoneNumber = ""
    override fun convert(helper: BaseViewHolder, item: SmsReport.ReportBean) {
        helper.setText(R.id.phoneNumber, phoneNumber)
        helper.setText(R.id.vourchTime, item.createDate)
        helper.setText(R.id.tvTime, item.createDate.substring(5, 7) + "/" + item.createDate.substring(8, 10))
    }
}