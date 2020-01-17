package com.stratagile.qlink.ui.adapter.topup

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.topup.Area
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.view.SmoothCheckBox
import java.math.BigDecimal

class TopupAbleAdapter(array: ArrayList<TopupProduct.ProductListBean>) : BaseQuickAdapter<TopupProduct.ProductListBean, BaseViewHolder>(R.layout.item_topup_able, array) {
    lateinit var payToken: PayToken.PayTokenListBean
    override fun convert(helper: BaseViewHolder, item: TopupProduct.ProductListBean) {
        helper.setText(R.id.tvFiat, item.localFiat)
        helper.setText(R.id.tvPrice, item.amountOfMoney.toString())
        helper.setText(R.id.deductionTokenSymbol, payToken.symbol)

        if (SpUtil.getInt(mContext, ConstantValue.Language, -1) == 0) {
            //英文
            helper.setText(R.id.explain, item.explainEn)
            helper.setText(R.id.tvAreaOperator, item.nameEn)
            helper.setText(R.id.description, item.descriptionEn)
            helper.setText(R.id.tvOperator, item.countryEn + item.provinceEn + item.ispEn)
        } else {
            helper.setText(R.id.tvAreaOperator, item.name)

            helper.setText(R.id.explain, item.explain)
            helper.setText(R.id.description, item.description)
            helper.setText(R.id.tvOperator, item.country + item.province + item.isp)
        }

        if ("FIAT".equals(item.payWay)) {
            //如果是法币支付
            helper.setText(R.id.price, item.payFiatAmount.toBigDecimal().multiply(item.discount.toBigDecimal()).stripTrailingZeros().toPlainString())
            helper.setText(R.id.deductionAmount, item.payFiatAmount.toBigDecimal().multiply(item.qgasDiscount.toBigDecimal()).divide(payToken.price.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString())

            helper.setGone(R.id.payTokenSymbol, false)
            if (SpUtil.getInt(mContext, ConstantValue.Language, -1) == 0) {
                helper.setGone(R.id.rmbCn, false)
                helper.setGone(R.id.rmbEn, true)
            } else {
                helper.setGone(R.id.rmbCn, true)
                helper.setGone(R.id.rmbEn, false)
            }
        } else {
            helper.setGone(R.id.rmbCn, false)
            helper.setGone(R.id.rmbEn, false)
            helper.setGone(R.id.payTokenSymbol, true)

            var deductionTokenPrice = 0.toDouble()
            if ("CNY".equals(item.payFiat)) {
                deductionTokenPrice = payToken.price
            } else if ("USD".equals(item.payFiat)){
                deductionTokenPrice = payToken.usdPrice
            }
            //如果是代币支付
            //当地法币金额是用来显示的，支付法币金额是用来计算的。

            //    抵扣币金额     =    原价*抵扣币折扣
            //    抵扣币数量     =    抵扣币金额/抵扣币价格

            //    支付法币金额  =    原价*产品折扣
            //    支付代币金额  =    支付法币金额-抵扣币金额
            //    支付代币数量  =    支付代币金额/支付代币价格
            var dikoubijine = item.payFiatAmount.toBigDecimal().multiply(item.qgasDiscount.toBigDecimal())
            var dikoubishuliang = dikoubijine.divide(deductionTokenPrice.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP)

            var zhifufabijine = item.payFiatAmount.toBigDecimal().multiply(item.discount.toBigDecimal())
            var zhifudaibijine = zhifufabijine - dikoubijine
            var zhifubishuliang = zhifudaibijine.divide(if ("CNY".equals(item.payFiat)){item.payTokenCnyPrice.toBigDecimal()} else {item.payTokenUsdPrice.toBigDecimal()}, 3, BigDecimal.ROUND_HALF_UP)

            helper.setText(R.id.price, zhifubishuliang.stripTrailingZeros().toPlainString())
            helper.setText(R.id.payTokenSymbol, item.payTokenSymbol)
            helper.setText(R.id.deductionAmount, dikoubishuliang.stripTrailingZeros().toPlainString())
        }
    }
}