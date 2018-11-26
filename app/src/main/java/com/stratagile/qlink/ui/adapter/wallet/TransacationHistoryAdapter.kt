package com.stratagile.qlink.ui.adapter.wallet

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.entity.TransactionInfo
import com.stratagile.qlink.utils.TimeUtil
import java.math.BigDecimal

class TransacationHistoryAdapter(arrayList: ArrayList<TransactionInfo>) : BaseQuickAdapter<TransactionInfo, BaseViewHolder>(R.layout.item_transaction_history, arrayList) {
    override fun convert(helper: BaseViewHolder, item: TransactionInfo) {
        helper.setText(R.id.tvTransactionHash, item.transationHash)
        helper.setText(R.id.tvTransactionStatus, item.transactionState)
        helper.setText(R.id.tvTransactionTime, TimeUtil.getTransactionTime(item.timestamp))
        var tokenValue = item.transactionValue.toDouble() / (Math.pow(10.0, item.tokenDecimals.toDouble()))
        if (item.from.equals(item.owner)) {
            helper.setText(R.id.tvTransactionValue, "- " + BigDecimal.valueOf(tokenValue) + " " + item.transactionToken)
            helper.setImageResource(R.id.ivTransactionType, R.mipmap.icons_transaction_out)
        } else {
            var bigDecimal = BigDecimal.valueOf(tokenValue)
            helper.setText(R.id.tvTransactionValue, "+ " + bigDecimal.toPlainString() + " " + item.transactionToken)
            helper.setImageResource(R.id.ivTransactionType, R.mipmap.icons_transaction_in)
        }
        helper.addOnClickListener(R.id.tvTransactionHash)
    }
}