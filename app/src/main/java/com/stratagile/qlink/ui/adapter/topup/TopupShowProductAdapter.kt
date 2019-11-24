package com.stratagile.qlink.ui.adapter.topup

import android.media.Image
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.MainAPI
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.view.SmoothCheckBox

class TopupShowProductAdapter(array: ArrayList<TopupProduct.ProductListBean>) : BaseQuickAdapter<TopupProduct.ProductListBean, BaseViewHolder>(R.layout.item_topup_show_product, array) {
    override fun convert(helper: BaseViewHolder, item: TopupProduct.ProductListBean) {
        if ("".equals(item.imgPath) || "/".equals(item.imgPath)) {
            helper.setImageResource(R.id.ivBack, R.mipmap.guangdong_mobile)
        } else {
            Glide.with(mContext)
                    .load(AppConfig.instance.baseUrl + item.imgPath.replace("/dapp", ""))
                    .apply(AppConfig.getInstance().optionsTopup)
                    .into(helper.getView(R.id.ivBack))
        }
        if (SpUtil.getInt(mContext, ConstantValue.Language, -1) == 0) {
            //英文
            helper.setText(R.id.tvOperator, item.country + item.province + item.isp)
            helper.setText(R.id.tvOperatorEn, item.countryEn + item.provinceEn+ item.ispEn)
            helper.setText(R.id.tvAreaOperator, item.name)
            helper.setText(R.id.tvAreaOperatorEn, item.nameEn)
            helper.setText(R.id.tvDiscount, (100 - item.discount * 100).toBigDecimal().stripTrailingZeros().toPlainString() + "%")
        } else {
            helper.setText(R.id.tvOperator, item.country + item.province + item.isp)
            helper.setText(R.id.tvOperatorEn, item.countryEn + item.provinceEn+ item.ispEn)
            helper.setText(R.id.tvAreaOperator, item.name)
            helper.setText(R.id.tvAreaOperatorEn, item.nameEn)
            helper.setText(R.id.tvDiscount, (item.discount * 10).toString())
        }
        if (item.stock == 0) {
            helper.setGone(R.id.noProduct, true)
        } else {
            helper.setGone(R.id.noProduct, false)
        }
    }
}