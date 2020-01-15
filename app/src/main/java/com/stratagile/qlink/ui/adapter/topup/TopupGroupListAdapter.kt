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
import com.stratagile.qlink.entity.topup.PayToken
import com.stratagile.qlink.entity.topup.TopupGroupKindList
import com.stratagile.qlink.entity.topup.TopupGroupList
import com.stratagile.qlink.utils.TimeUtil
import com.stratagile.qlink.utils.UIUtils
import java.math.BigDecimal

class TopupGroupListAdapter(array: ArrayList<TopupGroupList.GroupListBean>) : BaseQuickAdapter<TopupGroupList.GroupListBean, BaseViewHolder>(R.layout.item_topup_group, array) {
    lateinit var payToken: PayToken.PayTokenListBean
    override fun convert(helper: BaseViewHolder, item: TopupGroupList.GroupListBean) {
        var isCn = true
        helper.setText(R.id.tvNeedPartners, mContext.getString(R.string._more_partner_needed, (item.numberOfPeople - item.joined).toString()))
        helper.setText(R.id.tvGroupInfo, mContext.getString(R.string._off_discount_partners, if (isCn){item.discount.toBigDecimal().multiply(BigDecimal.TEN).stripTrailingZeros().toPlainString()} else {(1.toBigDecimal() - item.discount.toBigDecimal()).multiply(100.toBigDecimal()).stripTrailingZeros().toPlainString()}, item.numberOfPeople.toString()))
        helper.setText(R.id.tvRemainTime, TimeUtil.getOrderTime(TimeUtil.timeStamp(item.createDate) + (item.duration * 60 * 1000)))
        helper.addOnClickListener(R.id.tvJoinNow)
        var headList = arrayListOf<String>()
        helper.setVisible(R.id.tvJoinNow, true)
        item.items?.forEach {
            if (it.id.equals(ConstantValue.currentUser.userId)) {
                helper.setVisible(R.id.tvJoinNow, false)
            }
            headList.add(it.head)
        }
        headList.add(item.head)
        var llAvatar = helper.getView<RelativeLayout>(R.id.llAvatar)
        var allMargin = UIUtils.dip2px(20f, mContext)

        var leftMargin = 0
        if (headList.size > 1) {
            leftMargin = allMargin / (headList.size - 1)
        }
        headList.forEachIndexed { index, s ->
            var imageView = ImageView(mContext)
            var lp = RelativeLayout.LayoutParams(UIUtils.dip2px(32f, mContext), UIUtils.dip2px(32f, mContext))
            lp.leftMargin = index * leftMargin
            imageView.layoutParams = lp
            KLog.i(AppConfig.instance.baseUrl + s)
            Glide.with(mContext)
                    .load(MainAPI.MainBASE_URL + s)
//                    .load(AppConfig.instance.baseUrl + "/userfiles/1/images/topup/2019/12/a83bf1e7cd86413b896a1783a43b751f.png".replace("/dapp", ""))
                    .apply(AppConfig.getInstance().optionsWhiteColor)
                    .into(imageView)
            llAvatar.addView(imageView)


            if (index == headList.size - 1) {
                var imageView1 = ImageView(mContext)
                var lp1 = RelativeLayout.LayoutParams(UIUtils.dip2px(12f, mContext), UIUtils.dip2px(12f, mContext))
                lp1.leftMargin = index * leftMargin + (UIUtils.dip2px(32f, mContext) - UIUtils.dip2px(12f, mContext))
                lp1.topMargin = UIUtils.dip2px(32f, mContext) - UIUtils.dip2px(12f, mContext)
                imageView1.layoutParams = lp1
                Glide.with(mContext)
                        .load(R.mipmap.label_regimental)
                        .apply(RequestOptions().centerCrop())
                        .into(imageView1)
                llAvatar.addView(imageView1)
            }
        }
    }
}