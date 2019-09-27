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

class TopupShowProductAdapter(array: ArrayList<TopupProduct.ProductListBean>) : BaseQuickAdapter<TopupProduct.ProductListBean, BaseViewHolder>(R.layout.item_topup_show_product, array) {
    override fun convert(helper: BaseViewHolder, item: TopupProduct.ProductListBean) {
        helper.setText(R.id.tvDiscount, (item.discount * 10).toString())
        if (SpUtil.getInt(mContext, ConstantValue.Language, -1) == 0) {
            //英文
            helper.setText(R.id.tvOperator, item.countryEn + item.province + item.ispEn)
        } else {
            helper.setText(R.id.tvOperator, item.country + item.province + item.isp)
        }
    }
}