package com.stratagile.qlink.ui.adapter.defi

import android.util.Log
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.defi.DefiCategory
import com.stratagile.qlink.view.SmoothCheckBox

class DefiDetailCategoryAdapter(array: ArrayList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_defi_detail_category, array) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.defiCategory, item)
        when(item) {
            "Ethereum" -> {
                helper.setBackgroundColor(R.id.defiCategory, mContext.getColor(R.color.color_FF679D_26))
                helper.setTextColor(R.id.defiCategory, mContext.getColor(R.color.color_FF679D))
            }
            "Lending" -> {
                helper.setBackgroundColor(R.id.defiCategory, mContext.getColor(R.color.color_945BFF_26))
                helper.setTextColor(R.id.defiCategory, mContext.getColor(R.color.color_945BFF))
            }
            "Payments" -> {
                helper.setBackgroundColor(R.id.defiCategory, mContext.getColor(R.color.color_ED321_26))
                helper.setTextColor(R.id.defiCategory, mContext.getColor(R.color.color_ED321))
            }
            "Derivatives" -> {
                helper.setBackgroundColor(R.id.defiCategory, mContext.getColor(R.color.color_00C0FF_26))
                helper.setTextColor(R.id.defiCategory, mContext.getColor(R.color.color_00C0FF))
            }
            "DEXes" -> {
                helper.setBackgroundColor(R.id.defiCategory, mContext.getColor(R.color.color_FFB46A_26))
                helper.setTextColor(R.id.defiCategory, mContext.getColor(R.color.color_FFB46A))
            }
            "Assets" -> {
                helper.setBackgroundColor(R.id.defiCategory, mContext.getColor(R.color.color_0C66FF_26))
                helper.setTextColor(R.id.defiCategory, mContext.getColor(R.color.color_0C66FF))
            }
        }
    }
}