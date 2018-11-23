package com.stratagile.qlink.utils

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.Tpcs

class SendTokenAdapter(arrayList: ArrayList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_send_token, arrayList) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.tvContent, item)
    }

}