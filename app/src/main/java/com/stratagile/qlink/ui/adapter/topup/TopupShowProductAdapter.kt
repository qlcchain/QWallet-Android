package com.stratagile.qlink.ui.adapter.topup

import android.graphics.Paint
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.data.api.MainAPI
import com.stratagile.qlink.entity.reward.Dict
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.entity.topup.TopupGroupKindList
import com.stratagile.qlink.entity.topup.TopupProduct
import com.stratagile.qlink.utils.SpUtil
import com.stratagile.qlink.utils.TextUtil
import com.stratagile.qlink.utils.TimeUtil
import com.stratagile.qlink.utils.UIUtils
import java.math.BigDecimal

class TopupShowProductAdapter(array: ArrayList<TopupProduct.ProductListBean>) : BaseQuickAdapter<TopupProduct.ProductListBean, BaseViewHolder>(R.layout.item_topup_show_product, array) {
    lateinit var payToken: PayToken.PayTokenListBean
    lateinit var proxyAcitivtyDict : Dict
    lateinit var mustGroupKind : TopupGroupKindList.GroupKindListBean
    override fun convert(helper: BaseViewHolder, item: TopupProduct.ProductListBean) {
        var isCn = true
        isCn = SpUtil.getInt(mContext, ConstantValue.Language, -1) == 1
        if ("".equals(item.imgPath) || "/".equals(item.imgPath)) {
            Glide.with(mContext)
                    .load(R.mipmap.guangdong_mobile)
                    .apply(AppConfig.getInstance().optionsTopup)
                    .into(helper.getView(R.id.ivBack))
        } else {
            Glide.with(mContext)
                    .load(AppConfig.instance.baseUrl + item.imgPath.replace("/dapp", ""))
                    .apply(AppConfig.getInstance().optionsTopup)
                    .into(helper.getView(R.id.ivBack))
        }
        if (SpUtil.getInt(mContext, ConstantValue.Language, -1) == 0) {
            //英文
            //tvOperator
            // 国家 + 运营商 + 面额 + 单位 + "充值话费，1-72小时到账"
            TextUtil.setGroupInfo(item.countryEn, item.ispEn + " " + item.localFiatAmount + item.localFiat + "\n" + item.explainEn, helper.getView(R.id.tvOperator))
            //helper.setText(R.id.tvOperator, item.countryEn + item.ispEn + " " + item.localFiatAmount + item.localFiat + "\n" + item.explainEn)
//            helper.setText(R.id.tvOperatorEn, item.countryEn + item.provinceEn+ item.ispEn)
//            helper.setText(R.id.tvAreaOperator, item.name)
//            helper.setText(R.id.tvAreaOperatorEn, item.nameEn)
        } else {
            //tvOperator
            // 国家 + 运营商 + 面额 + 单位 + "充值话费，1-72小时到账"
            TextUtil.setGroupInfo(item.country, item.isp +  " " + item.localFiatAmount + item.localFiat +  "\n" +item.explain, helper.getView(R.id.tvOperator))
            //helper.setText(R.id.tvOperator, item.country + item.isp +  " " + item.localFiatAmount + item.localFiat +  "\n" +item.explain)
        }
        helper.getView<TextView>(R.id.tvPriceOrgin).paint.isAntiAlias = true
        helper.getView<TextView>(R.id.tvPriceOrgin).paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        if ("FIAT".equals(item.payWay)) {
            //如果是法币支付
            if (item.orderTimes < 100) {
                helper.setText(R.id.tvSaleVolume, mContext.getString(R.string.xxx_sold, "100+"))
            } else {
                helper.setText(R.id.tvSaleVolume, mContext.getString(R.string.xxx_sold, item.orderTimes.toString()))
            }
            if (isCn) {
                helper.setText(R.id.tvDiscount, mContext.getString(R.string.limited_to_offering_of_xx_off_discount, item.discount.toBigDecimal().multiply(BigDecimal.TEN).stripTrailingZeros().toPlainString()))
            } else {
                helper.setText(R.id.tvDiscount, mContext.getString(R.string.limited_to_offering_of_xx_off_discount, (1.toBigDecimal() - item.discount.toBigDecimal()).multiply(BigDecimal.TEN.multiply(BigDecimal.TEN)).stripTrailingZeros().toPlainString()))
            }
            if ("CNY".equals(item.payFiat)) {
                helper.setText(R.id.tvPrice, "¥" + item.payFiatAmount.toBigDecimal().multiply(item.discount.toBigDecimal()).stripTrailingZeros().toPlainString() + "+" + item.payFiatAmount.toBigDecimal().multiply(item.qgasDiscount.toBigDecimal()).divide(payToken.price.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + payToken.symbol)
                helper.setText(R.id.tvPriceOrgin, "¥" + item.payFiatAmount.toBigDecimal().stripTrailingZeros().toPlainString())
            } else if ("USD".equals(item.payFiat)){
                helper.setText(R.id.tvPrice, "$" + item.payFiatAmount.toBigDecimal().multiply(item.discount.toBigDecimal()).stripTrailingZeros().toPlainString() + "+" + item.payFiatAmount.toBigDecimal().multiply(item.qgasDiscount.toBigDecimal()).divide(payToken.price.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + payToken.symbol)
                helper.setText(R.id.tvPriceOrgin, "$" + item.payFiatAmount.toBigDecimal().stripTrailingZeros().toPlainString())
            }
        } else {
            if (item.orderTimes < 100) {
                helper.setText(R.id.tvSaleVolume, mContext.getString(R.string.xxx_open, "100+"))
            } else {
                helper.setText(R.id.tvSaleVolume, mContext.getString(R.string.xxx_open, item.orderTimes.toString()))
            }
            if (this::proxyAcitivtyDict.isInitialized && TimeUtil.timeStamp(proxyAcitivtyDict.data.topupGroupStartDate) < proxyAcitivtyDict.currentTimeMillis && (TimeUtil.timeStamp(proxyAcitivtyDict.data.topopGroupEndDate) > proxyAcitivtyDict.currentTimeMillis)) {
                if (isCn) {
                    helper.setText(R.id.tvDiscount, mContext.getString(R.string.up_to_xx_off, mustGroupKind.discount.toBigDecimal().multiply(BigDecimal.TEN).stripTrailingZeros().toPlainString()))
                } else {
                    helper.setText(R.id.tvDiscount, mContext.getString(R.string.up_to_xx_off, (1.toBigDecimal() - mustGroupKind.discount.toBigDecimal()).multiply(BigDecimal.TEN.multiply(BigDecimal.TEN)).stripTrailingZeros().toPlainString()))
                }
            } else {
                if (isCn) {
                    helper.setText(R.id.tvDiscount, mContext.getString(R.string.limited_to_offering_of_xx_off_discount, item.discount.toBigDecimal().multiply(BigDecimal.TEN).stripTrailingZeros().toPlainString()))
                } else {
                    helper.setText(R.id.tvDiscount, mContext.getString(R.string.limited_to_offering_of_xx_off_discount, (1.toBigDecimal() - item.discount.toBigDecimal()).multiply(BigDecimal.TEN.multiply(BigDecimal.TEN)).stripTrailingZeros().toPlainString()))
                }
            }

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
            var dikoubijine = item.payFiatAmount.toBigDecimal().multiply(item.qgasDiscount.toBigDecimal().multiply(mustGroupKind.discount.toBigDecimal()))
            var dikoubishuliang = dikoubijine.divide(deductionTokenPrice.toBigDecimal(), 3, BigDecimal.ROUND_HALF_UP)

            var zhifufabijine = item.payFiatAmount.toBigDecimal().multiply(mustGroupKind.discount.toBigDecimal())
            var zhifudaibijine = zhifufabijine - dikoubijine
            var zhifubishuliang = zhifudaibijine.divide(if ("CNY".equals(item.payFiat)){item.payTokenCnyPrice.toBigDecimal()} else {item.payTokenUsdPrice.toBigDecimal()}, 3, BigDecimal.ROUND_HALF_UP)
            helper.setText(R.id.tvPrice, zhifubishuliang.stripTrailingZeros().toPlainString() + item.payTokenSymbol + "+" + dikoubishuliang.stripTrailingZeros().toPlainString() + payToken.symbol)


            var orginZhifufabishuliang = item.payFiatAmount.toBigDecimal().multiply(item.discount.toBigDecimal()).divide(if ("CNY".equals(item.payFiat)){item.payTokenCnyPrice.toBigDecimal()} else {item.payTokenUsdPrice.toBigDecimal()}, 3, BigDecimal.ROUND_HALF_UP)
            helper.setText(R.id.tvPriceOrgin, orginZhifufabishuliang.stripTrailingZeros().toPlainString() + item.payTokenSymbol)
        }

        if (item.stock == 0) {
            helper.setGone(R.id.noProduct, true)
        } else {
            helper.setGone(R.id.noProduct, false)
        }


        var llAvatar = helper.getView<RelativeLayout>(R.id.llAvatar)
        var allMargin = UIUtils.dip2px(20f, mContext)

        var leftMargin = 0
        if ("yes".equals(item.haveGroupBuy)) {
            if (item.items.size > 1) {
                leftMargin = allMargin / (item.items.size - 1)
            }
            item.items.forEachIndexed { index, s ->
                var imageView = ImageView(mContext)
                var lp = RelativeLayout.LayoutParams(UIUtils.dip2px(24f, mContext), UIUtils.dip2px(24f, mContext))
                lp.leftMargin = index * leftMargin
                imageView.layoutParams = lp
                KLog.i(AppConfig.instance.baseUrl + s.head)
                Glide.with(mContext)
                        .load(MainAPI.MainBASE_URL + s.head)
                        .apply(AppConfig.getInstance().optionsWhiteColor)
                        .into(imageView)
                llAvatar.addView(imageView)
            }
        }
    }
}