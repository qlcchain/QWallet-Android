package com.stratagile.qlink.ui.adapter.topup

import android.support.v7.widget.CardView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.MainAPI
import com.stratagile.qlink.entity.topup.GroupItemList
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.entity.topup.TopupGroupKindList
import com.stratagile.qlink.entity.topup.TopupGroupList
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.TimeUtil
import com.stratagile.qlink.utils.UIUtils
import java.math.BigDecimal

class TopupGroupItemAdapter(array: ArrayList<GroupItemList.ItemListBean>) : BaseQuickAdapter<GroupItemList.ItemListBean, BaseViewHolder>(R.layout.item_topup_group_list, array) {
    lateinit var payToken: PayToken.PayTokenListBean
    override fun convert(helper: BaseViewHolder, item: GroupItemList.ItemListBean) {
        helper.setText(R.id.tvOrderTime, TimeUtil.timeConvert(item.createDate))
        helper.setText(R.id.phoneNumber, item.product.globalRoaming + item.phoneNumber)
        helper.setText(R.id.price, item.product.localFiatMoney + item.product.localFiat)
        helper.setText(R.id.txid, item.deductionTokenInTxid)
        helper.setText(R.id.tvDeductionToken, item.deductionTokenAmount.toString()+ item.deductionToken)

        helper.setGone(R.id.orderOpreate, false)
        helper.setGone(R.id.voucherDetail, false)
        helper.setGone(R.id.cancelOrder, false)
        helper.setGone(R.id.tvPayNow, false)

        helper.setGone(R.id.llSerialNumber, false)
        helper.setGone(R.id.llPinNumber, false)
        helper.setGone(R.id.llPasswordNumber, false)
        helper.setGone(R.id.llExpiryDate, false)

        helper.addOnClickListener(R.id.cancelOrder)
        helper.addOnClickListener(R.id.tvPayNow)
        helper.addOnClickListener(R.id.llVoucher)
        helper.addOnClickListener(R.id.voucherDetail)

        helper.setText(R.id.payToken, item.payTokenAmount.toBigDecimal().stripTrailingZeros().toPlainString() + item.payToken)

        if (SpUtil.getInt(mContext, ConstantValue.Language, -1) == 0) {
            //英文
            helper.setText(R.id.opreator, item.product.countryEn + item.product.provinceEn + item.product.ispEn + "-" + item.product.productNameEn)
            helper.setImageResource(R.id.ivInvoiceDetail, R.mipmap.background_reim_en)
        } else {
            helper.setText(R.id.opreator, item.product.country + item.product.province + item.product.isp + "-" + item.product.productName)
            helper.setImageResource(R.id.ivInvoiceDetail, R.mipmap.background_reim)
        }

//        if (item.serialno != null && !"".equals(item.serialno)) {
//            helper.setGone(R.id.llSerialNumber, true)
//            helper.setText(R.id.tvSerialNumber, item.serialno)
//        } else {
//            helper.setGone(R.id.llSerialNumber, false)
//        }
//        if (item.pin != null && !"".equals(item.pin)) {
//            helper.setGone(R.id.llPinNumber, true)
//            helper.setText(R.id.tvPinNumber, item.pin)
//        } else {
//            helper.setGone(R.id.llPinNumber, false)
//        }
//        if (item.passwd != null && !"".equals(item.passwd)) {
//            helper.setGone(R.id.llPasswordNumber, true)
//            helper.setText(R.id.tvPasswordNumber, item.passwd)
//        } else {
//            helper.setGone(R.id.llPasswordNumber, false)
//        }
//        if (item.expiredTime != null && !"".equals(item.expiredTime)) {
//            helper.setGone(R.id.llExpiryDate, true)
//            helper.setText(R.id.tvExpiryDate, item.expiredTime)
//        } else {
//            helper.setGone(R.id.llExpiryDate, false)
//        }
        when(item.status) {
            "NEW" -> {
                helper.setGone(R.id.orderOpreate, true)
                helper.setGone(R.id.cancelOrder, false)
                helper.setGone(R.id.tvPayNow, true)
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_is_not_in_the_accounts, item.deductionToken))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
                if (!"".equals(item.deductionTokenInTxid)) {
                    helper.setGone(R.id.orderOpreate, false)
                    helper.setGone(R.id.cancelOrder, false)
                    helper.setGone(R.id.tvPayNow, false)
                    helper.setText(R.id.orderState, mContext.getString(R.string.wait_public_chain_confirmation))
                    helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
                }
            }
            "DEDUCTION_TOKEN_PAID" -> {
                helper.setGone(R.id.orderOpreate, true)
                helper.setGone(R.id.cancelOrder, false)
                helper.setGone(R.id.tvPayNow, true)
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_paid_not_in_the_telephone_fee))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))

                if (item.payTokenInTxid != null && !"".equals(item.payTokenInTxid)) {
                    helper.setGone(R.id.orderOpreate, false)
                    helper.setGone(R.id.cancelOrder, false)
                    helper.setGone(R.id.tvPayNow, false)
                    helper.setText(R.id.orderState, mContext.getString(R.string.wait_public_chain_confirmation))
                    helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
                }
            }
            "PAY_TOKEN_PAID" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.paytoken_paid))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
//            "RECHARGE" -> {
//                helper.setText(R.id.orderState, mContext.getString(R.string.recharge))
//                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_108ee9))
//            }
//            "SUCCESS" -> {
//                helper.setVisible(R.id.voucherDetail, true)
//                helper.setText(R.id.orderState, mContext.getString(R.string.recharge_success))
//                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_01b5ab))
//            }
//            "FAIL" -> {
//                helper.setText(R.id.orderState, mContext.getString(R.string.failure_to_pay))
//                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
//            }
            "DEDUCTION_TXID_ERROR" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_resolution_failure))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "PAY_TXID_ERROR" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_resolution_failure))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "CANCEL" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.canceled))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "TIME_OUT_DOWN" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.order_time_out_down))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }
            "TIME_OUT" -> {
                helper.setText(R.id.orderState, mContext.getString(R.string.order_time_out))
                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
            }

//            "QGAS_RETURNED" -> {
//                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_has_been_returned, item.symbol))
//                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
//            }
//            "PAY_TOKEN_RETURNED" -> {
//                helper.setText(R.id.orderState, mContext.getString(R.string.qgas_has_been_returned, item.payTokenSymbol))
//                helper.setTextColor(R.id.orderState, mContext.resources.getColor(R.color.color_ff3669))
//            }
        }
        helper.setVisible(R.id.payIntroduce, true)
    }
}