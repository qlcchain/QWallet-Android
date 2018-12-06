package com.stratagile.qlink.ui.adapter.market

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.view.SmoothCheckBox

class MarketTokenAdapter(array: ArrayList<TokenSelect>) : BaseQuickAdapter<TokenSelect, BaseViewHolder>(R.layout.item_select_token, array) {
    override fun convert(helper: BaseViewHolder, item: TokenSelect) {
        helper.setText(R.id.tokenName, item.tokenName + "/BTC")
        var checkBox = helper.getView<SmoothCheckBox>(R.id.checkBox)
        checkBox.setChecked(item.isSelect)
        checkBox.setOnClickListener {
            item.isSelect = !item.isSelect
            notifyItemChanged(helper.layoutPosition)
        }
    }
}