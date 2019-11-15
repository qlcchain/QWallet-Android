package com.stratagile.qlink.ui.adapter.mining

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.socks.library.KLog
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.data.api.MainAPI
import com.stratagile.qlink.entity.TokenSelect
import com.stratagile.qlink.entity.mining.MiningIndex
import com.stratagile.qlink.entity.mining.MiningRank
import com.stratagile.qlink.utils.AccountUtil
import com.stratagile.qlink.view.SmoothCheckBox

class MiningRankAdapter(array: ArrayList<MiningIndex.RewardRankingsBean>) : BaseQuickAdapter<MiningIndex.RewardRankingsBean, BaseViewHolder>(R.layout.item_mining_rank, array) {
    override fun convert(helper: BaseViewHolder, item: MiningIndex.RewardRankingsBean) {
        helper.setText(R.id.userName, AccountUtil.setUserNickName(item.name))
        helper.setText(R.id.tvRank, item.sequence.toString() + "")
        helper.setText(R.id.invitePersons, item.totalReward.toBigDecimal().stripTrailingZeros().toPlainString() + " QLC")
        Glide.with(mContext)
                .load(MainAPI.MainBASE_URL + item.head)
                .apply(AppConfig.getInstance().options)
                .into(helper.getView<View>(R.id.userAvatar) as ImageView)
    }
}