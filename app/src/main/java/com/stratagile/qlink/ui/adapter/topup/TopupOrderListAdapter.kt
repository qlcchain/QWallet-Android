package com.stratagile.qlink.ui.adapter.topup

import android.content.Intent
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.topup.TopupOrder
import com.stratagile.qlink.entity.topup.TopupOrderList
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.ui.activity.topup.TopupPayNeoChainActivity
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.TimeUtil
import com.stratagile.qlink.view.SmoothCheckBox

class TopupOrderListAdapter(array: ArrayList<TopupOrder.OrderBean>) : BaseQuickAdapter<TopupOrder.OrderBean, BaseViewHolder>(R.layout.item_topup_order_list, array) {
    override fun convert(helper: BaseViewHolder, item: TopupOrder.OrderBean) {
        helper.setText(R.id.tvOrderTime, TimeUtil.timeConvert(item.orderTime))
        helper.setText(R.id.phoneNumber, item.areaCode + item.phoneNumber)
        helper.setText(R.id.price, item.localFiatMoney + item.localFiat)
        helper.setText(R.id.txid, item.txid)
        helper.setText(R.id.tvDeductionToken, item.qgasAmount.toBigDecimal().stripTrailingZeros().toPlainString() + item.symbol)

        helper.setGone(R.id.orderOpreate, false)
        helper.setGone(R.id.voucherDetail, false)
        helper.setGone(R.id.cancelOrder, false)
        helper.setGone(R.id.tvPayNow, false)
        helper.addOnClickListener(R.id.cancelOrder)
        helper.addOnClickListener(R.id.tvPayNow)
        helper.addOnClickListener(R.id.llVoucher)
        helper.addOnClickListener(R.id.voucherDetail)

        if ("TOKEN".equals(item.payWay)) {
            helper.setText(R.id.payToken, item.payTokenAmount.toBigDecimal().stripTrailingZeros().toPlainString() + item.payTokenSymbol)

        } else {
            helper.setText(R.id.payToken, item.discountPrice.toString() + item.payFiat)

        }

        if (SpUtil.getInt(mContext, ConstantValue.Language, -1) == 0) {
            //英文
            helper.setText(R.id.opreator, item.productCountryEn + item.productProvinceEn + item.productIspEn + "-" + item.productNameEn)
            helper.setImageResource(R.id.ivInvoiceDetail, R.mipmap.background_reim_en)
        } else {
            helper.setText(R.id.opreator, item.productCountry + item.productProvince + item.productIsp + "-" + item.productName)
            helper.setImageResource(R.id.ivInvoiceDetail, R.mipmap.background_reim)
        }

        if (item.serialno != null && !"".equals(item.serialno)) {
            helper.setGone(R.id.llSerialNumber, true)
            helper.setText(R.id.tvSerialNumber, item.serialno)
        } else {
            helper.setGone(R.id.llSerialNumber, false)
        }
        if (item.pin != null && !"".equals(item.pin)) {
            helper.setGone(R.id.llPinNumber, true)
            helper.setText(R.id.tvPinNumber, item.pin)
        } else {
            helper.setGone(R.id.llPinNumber, false)
        }
        if (item.passwd != null && !"".equals(item.passwd)) {
            helper.setGone(R.id.llPasswordNumber, true)
            helper.setText(R.id.tvPasswordNumber, item.passwd)
        } else {
            helper.setGone(R.id.llPasswordNumber, false)
        }
        if (item.expiredTime != null && !"".equals(item.expiredTime)) {
            helper.setGone(R.id.llExpiryDate, true)
            helper.setText(R.id.tvExpiryDate, item.expiredTime)
        } else {
            helper.setGone(R.id.llExpiryDate, false)
        }
        when(item.status) {
            "NEW" -> {
                helper.setGone(R.id.orderOpreate, true)
                helper.setGone(R.id.cancelOrder, true)
                helper.setGone(R.id.tvPayNow, true)
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_is_not_in_the_accounts, item.symbol))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
                if (!"".equals(item.txid)) {
                    helper.setGone(R.id.orderOpreate, false)
                    helper.setGone(R.id.cancelOrder, false)
                    helper.setGone(R.id.tvPayNow, false)
                    helper.setText(R.id.orderState, mContext.getString(R.string.wait_public_chain_confirmation))
                    helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
                }
            }
            "QGAS_PAID" -> {
                helper.setGone(R.id.orderOpreate, true)
                helper.setGone(R.id.cancelOrder, true)
                helper.setGone(R.id.tvPayNow, true)
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_paid_not_in_the_telephone_fee))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))

                if ("TOKEN".equals(item.payWay)) {
                    if (item.payTokenInTxid != null && !"".equals(item.payTokenInTxid)) {
                        helper.setGone(R.id.orderOpreate, false)
                        helper.setGone(R.id.cancelOrder, false)
                        helper.setGone(R.id.tvPayNow, false)
                        helper.setText(R.id.orderState, mContext.getString(R.string.wait_public_chain_confirmation))
                        helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
                    }
                } else {

                }
            }
            "PAY_TOKEN_PAID" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.paytoken_paid))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "RECHARGE" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.recharge))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_108ee9))
            }
            "SUCCESS" -> {
                helper.setVisible(R.id.voucherDetail, true)
                helper.setText(R.id.orderState, mContext.getString(R.string.recharge_success))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_01b5ab))
            }
            "FAIL" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.failure_to_pay))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "DEDUCTION_TXID_ERROR" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_resolution_failure))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "PAYTXID_ERROR" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_resolution_failure))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "CANCEL" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.canceled))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }

            "QGAS_RETURNED" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_has_been_returned, item.symbol))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "PAY_TOKEN_RETURNED" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_has_been_returned, item.payTokenSymbol))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
        }
        helper.setVisible(R.id.payIntroduce, true)
    }
}