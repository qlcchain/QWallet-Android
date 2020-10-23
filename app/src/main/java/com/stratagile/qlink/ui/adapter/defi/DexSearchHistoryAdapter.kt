package com.stratagile.qlink.ui.adapter.defi

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.db.DefiSearchHistory
import com.stratagile.qlink.db.SwapRecord
import com.stratagile.qlink.entity.defi.DexEntity
import com.stratagile.qlink.utils.TimeUtil
import kotlinx.android.synthetic.main.activity_swap_detail.*

class DexSearchHistoryAdapter(array: MutableList<DefiSearchHistory>) : BaseQuickAdapter<DefiSearchHistory, BaseViewHolder>(R.layout.item_dex_search_history, array) {
    override fun convert(helper: BaseViewHolder, item: DefiSearchHistory) {
        helper.setText(R.id.tvName, item.url)
        helper.addOnClickListener(R.id.ivDelete)
    }
}