package com.stratagile.qlink.ui.adapter.topup

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.topup.TopupOrderList
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.view.SmoothCheckBox

class TopupOrderListAdapter(array: ArrayList<TopupOrderList.OrderListBean>) : BaseQuickAdapter<TopupOrderList.OrderListBean, BaseViewHolder>(R.layout.item_topup_order_list, array) {
    override fun convert(helper: BaseViewHolder, item: TopupOrderList.OrderListBean) {
        helper.setText(R.id.tvOrderTime, item.orderTime)
        helper.setText(R.id.phoneNumber, item.areaCode + item.phoneNumber)
        helper.setText(R.id.price, item.originalPrice.toString())
        helper.setText(R.id.payQgas, item.qgasAmount.toBigDecimal().stripTrailingZeros().toPlainString() + "QGAS")
        helper.setText(R.id.payPrice, item.discountPrice.toString())
        if (SpUtil.getInt(mContext, ConstantValue.Language, -1) == 0) {
            //英文
            helper.setText(R.id.opreator, item.productCountryEn + item.productIspEn + "-" + item.productNameEn)
        } else {
            helper.setText(R.id.opreator, item.productCountry + item.productIsp + "-" + item.productName)
        }
        when(item.status) {
            "NEW" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_is_not_in_the_accounts))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "QGAS_PAID" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_paid_not_in_the_telephone_fee))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "RECHARGE" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.recharge))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_108ee9))
            }
            "SUCCESS" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.recharge_success))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_01b5ab))
            }
            "FAIL" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.failure_to_pay))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "ERROR" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_resolution_failure))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "QGAS_RETURNED" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_has_been_returned))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
        }
    }
}