package com.stratagile.qlink.ui.adapter.finance

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.data.api.MainAPI
import com.stratagile.qlink.entity.topup.SalePartner.UserListBean
import com.stratagile.qlink.utils.AccountUtil

class SalePartnerAdapter(data: List<UserListBean?>?) : BaseQuickAdapter<UserListBean, BaseViewHolder>(R.layout.item_sale_partner, data) {
    override fun convert(helper: BaseViewHolder, item: UserListBean) {
        helper.setText(R.id.userName, AccountUtil.setUserNickName(item.name))
        helper.setText(R.id.invitePersons, "+" + item.totalReward.toBigDecimal().stripTrailingZeros().toPlainString() + " QGAS")
        Glide.with(mContext)
                .load(MainAPI.MainBASE_URL + item.head)
                .apply(AppConfig.getInstance().options)
                .into((helper.getView<View>(R.id.userAvatar) as ImageView))
        if (item.level < 7) {
            //充值佣金
            helper.setText(R.id.rewardType, mContext.getString(R.string.recharge_commission))
        } else {
            //团购佣金
            helper.setText(R.id.rewardType, mContext.getString(R.string.group_plan_commission))
        }
    }
}