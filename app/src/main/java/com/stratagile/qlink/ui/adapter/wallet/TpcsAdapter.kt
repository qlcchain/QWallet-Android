package com.stratagile.qlink.ui.adapter.wallet

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.constant.ConstantValue
import com.stratagile.qlink.entity.AllWallet
import com.stratagile.qlink.entity.Tpcs
import com.stratagile.qlink.entity.TransactionInfo
import com.stratagile.qlink.utils.TimeUtil
import java.math.BigDecimal

class TpcsAdapter(arrayList: ArrayList<Tpcs.DataBean>) : BaseQuickAdapter<Tpcs.DataBean, BaseViewHolder>(R.layout.item_tpcs, arrayList) {
    var walletTpye = AllWallet.WalletType.NeoWallet
    override fun convert(helper: BaseViewHolder, item: Tpcs.DataBean) {
        helper.setText(R.id.tvTokenName, item.symbol)
        var imageView = helper.getView<ImageView>(R.id.ivTokenAvatar)
        if (walletTpye == AllWallet.WalletType.NeoWallet) {
            Glide.with(mContext)
                    .load(mContext.resources.getIdentifier(item.symbol.toLowerCase(), "mipmap", mContext.packageName))
                    .into(imageView)
        } else if (walletTpye == AllWallet.WalletType.EthWallet) {
            Glide.with(mContext)
                    .load(mContext.resources.getIdentifier(item.symbol.toLowerCase(), "mipmap", mContext.packageName))
                    .into(imageView)
        }else if (walletTpye == AllWallet.WalletType.EosWallet) {
            Glide.with(mContext)
                    .load(mContext.resources.getIdentifier(item.symbol.toLowerCase(), "mipmap", mContext.packageName))
                    .into(imageView)
        }
        helper.setText(R.id.tvRiseOrDown, item.priceChangePercent + "%")
        helper.setText(R.id.tvTokenValue, item.lastPrice)
        helper.setText(R.id.tvTokenMoney, ConstantValue.currencyBean.currencyImg + " " + item.coinVal.toString())
        if (item.priceChangePercent.contains("-")) {
            helper.setBackgroundRes(R.id.tvRiseOrDown, R.drawable.market_down)
        } else {
            helper.setBackgroundRes(R.id.tvRiseOrDown, R.drawable.market_rise)
        }
    }
}