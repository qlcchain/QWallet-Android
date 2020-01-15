package com.stratagile.qlink.ui.adapter.topup

import android.support.v7.widget.CardView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.entity.topup.TopupGroupKindList
import java.math.BigDecimal

class TopupGroupKindListAdapter(array: ArrayList<TopupGroupKindList.GroupKindListBean>) : BaseQuickAdapter<TopupGroupKindList.GroupKindListBean, BaseViewHolder>(R.layout.item_topup_group_kind, array) {
    lateinit var payToken: PayToken.PayTokenListBean
    override fun convert(helper: BaseViewHolder, item: TopupGroupKindList.GroupKindListBean) {
        var isCn = true
        helper.setText(R.id.tvContent, mContext.getString(R.string._off_discount_partners, if (isCn){item.discount.toBigDecimal().multiply(BigDecimal.TEN).stripTrailingZeros().toPlainString()} else {(1.toBigDecimal() - item.discount.toBigDecimal()).multiply(100.toBigDecimal()).stripTrailingZeros().toPlainString()}, item.numberOfPeople.toString()))
//        helper.setText(R.id.tvContent, (1.toBigDecimal() - item.discount.toBigDecimal()).multiply(100.toBigDecimal()).stripTrailingZeros().toPlainString() + "%off," + item.numberOfPeople + " discount partners")
        if (item.isSelected) {
            helper.getView<CardView>(R.id.cardView).setCardBackgroundColor(mContext.resources.getColor(R.color.mainColor))
            helper.setTextColor(R.id.tvContent, mContext.resources.getColor(R.color.white))
        } else {
            helper.getView<CardView>(R.id.cardView).setCardBackgroundColor(mContext.resources.getColor(R.color.color_f5f5f5))
            helper.setTextColor(R.id.tvContent, mContext.resources.getColor(R.color.color_1e1e24))
        }
    }
}