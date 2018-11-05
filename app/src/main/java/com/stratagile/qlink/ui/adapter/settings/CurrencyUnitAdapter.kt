package com.stratagile.qlink.ui.adapter.settings

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.CurrencyBean

class CurrencyUnitAdapter(arrayList : ArrayList<CurrencyBean>) : BaseQuickAdapter<CurrencyBean, BaseViewHolder>(R.layout.item_currency_unit, arrayList) {
    override fun convert(helper: BaseViewHolder, item: CurrencyBean) {
        helper.setText(R.id.tvCurrencyName, item.name)
        helper.setVisible(R.id.ivCheck, item.isCheck)
    }
}