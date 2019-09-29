package com.stratagile.qlink.ui.adapter.topup

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.view.SmoothCheckBox

class TopupAbleAdapter(array: ArrayList<TopupProduct.ProductListBean>) : BaseQuickAdapter<TopupProduct.ProductListBean, BaseViewHolder>(R.layout.item_topup_able, array) {
    override fun convert(helper: BaseViewHolder, item: TopupProduct.ProductListBean) {
        helper.setText(R.id.tvAreaOperator, item.name)
        helper.setText(R.id.tvPrice, item.price.toString())
        helper.setText(R.id.price, item.price.toBigDecimal().multiply(item.discount.toBigDecimal()).stripTrailingZeros().toPlainString())
        helper.setText(R.id.qgasCount, item.price.toBigDecimal().multiply(item.qgasDiscount.toBigDecimal()).stripTrailingZeros().toPlainString())
        if (SpUtil.getInt(mContext, ConstantValue.Language, -1) == 0) {
            //英文
            helper.setText(R.id.explain, item.explainEn)
            helper.setText(R.id.description, item.descriptionEn)
            helper.setGone(R.id.rmbCn, true)
            helper.setGone(R.id.rmbEn, false)
            helper.setText(R.id.tvOperator, item.countryEn + item.provinceEn + item.ispEn)
        } else {
            helper.setGone(R.id.rmbCn, true)
            helper.setGone(R.id.rmbEn, false)
            helper.setText(R.id.explain, item.explain)
            helper.setText(R.id.description, item.description)
            helper.setText(R.id.tvOperator, item.country + item.province + item.isp)
            helper.setText(R.id.tvOperator, item.country + item.province + item.isp)
        }
    }
}