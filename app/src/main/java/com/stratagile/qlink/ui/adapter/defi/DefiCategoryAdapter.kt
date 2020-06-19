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

class DefiCategoryAdapter(array: ArrayList<DefiCategoryEntity>) : BaseQuickAdapter<DefiCategoryEntity, BaseViewHolder>(R.layout.item_defi_category, array) {
    override fun convert(helper: BaseViewHolder, item: DefiCategoryEntity) {
        helper.setText(R.id.defiCategory, item.categoryName)
        var defiCategory = helper.getView<TextView>(R.id.defiCategory)
        defiCategory.isSelected = item.isSelect
        if (item.isSelect) {
            KLog.i("选中的为：" + helper.layoutPosition)
            defiCategory.setTextColor(mContext.resources.getColor(R.color.white))
        } else {
            defiCategory.setTextColor(mContext.resources.getColor(R.color.color_1e1e24))
        }
        helper.addOnClickListener(R.id.defiCategory)
    }
}