package com.stratagile.qlink.ui.adapter.mainwallet;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.Record;

import java.util.List;







/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: $description  adapter of MainTransactionRecordFragment
 * @date 2018/06/13 20:52:12
 */

public class MainTransactionRecordListAdapter extends BaseQuickAdapter<Record.DataBean, BaseViewHolder> {

    private String currentWalletAddress;

    public String getCurrentWalletAddress() {
        return currentWalletAddress;
    }

    public void setCurrentWalletAddress(String currentWalletAddress) {
        this.currentWalletAddress = currentWalletAddress;
    }
    public MainTransactionRecordListAdapter(@Nullable List<Record.DataBean> data) {
       super(R.layout.transaction_history_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Record.DataBean item) {
        //type = 0 是wifi， type = 1 是转账给别人，type = 2 是用neo兑换为qlc  3是vpn连接扣费 4 wifi注册扣费 、 5 vpn注册扣费
        TextView tvDesc = helper.getView(R.id.desc);
        Drawable drawable;
        switch (item.getType()) {
            case 0:
                helper.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_green_logo));
                helper.setText(R.id.type, item.getWifiName());
                helper.setText(R.id.rate, "");
                tvDesc.setText("QLC");
                if (item.getIsGratuity().equals("NohaveAreward")) {
                    helper.setVisible(R.id.ll_right, false);
                    helper.setVisible(R.id.iv_right, true);
                    helper.setImageDrawable(R.id.iv_right, mContext.getResources().getDrawable(R.mipmap.img_history_pentagram_two));
                } else {
                    helper.setVisible(R.id.ll_right, true);
                    helper.setVisible(R.id.iv_right, false);
                    if (item.getAddressFrom().equals(currentWalletAddress)) {
                        helper.setText(R.id.shuzi, "-" + item.getQlc() + "");
                        drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_falling);
                    } else {
                        helper.setText(R.id.shuzi, "+" + item.getQlc() + "");
                        drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_rise);
                    }
                    /// 这一步必须要做,否则不会显示.
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tvDesc.setCompoundDrawables(drawable, null, null, null);
                    tvDesc.setCompoundDrawablePadding(20);
                    tvDesc.setText("QLC");
                }
                break;
            case 1:
                helper.setVisible(R.id.ll_right, true);
                helper.setVisible(R.id.iv_right, false);
                helper.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_purchase));
                helper.setText(R.id.type, "QLC");
                helper.setText(R.id.rate, "");
                if (item.getAddressFrom().equals(currentWalletAddress)) {
                    helper.setText(R.id.shuzi, "-" + item.getQlc() + "");
                    drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_falling);
                } else {
                    helper.setText(R.id.shuzi, "+" + item.getQlc() + "");
                    drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_rise);
                }
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvDesc.setCompoundDrawables(drawable, null, null, null);
                tvDesc.setCompoundDrawablePadding(20);
                tvDesc.setText("QLC");
                break;
            case 2:
                helper.setVisible(R.id.ll_right, true);
                helper.setVisible(R.id.iv_right, false);
                helper.setText(R.id.type, "NEO/QLC Purchase");
                helper.setText(R.id.rate, "1NEO = " + item.getExchangeRate() + " QLC");
                helper.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_purchase));
                helper.setText(R.id.shuzi, "+" + item.getExchangeOfQlc());
                drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_rise);
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvDesc.setCompoundDrawables(drawable, null, null, null);
                tvDesc.setCompoundDrawablePadding(20);
                tvDesc.setText("QLC");
                break;
            case 3:
                helper.setVisible(R.id.ll_right, true);
                helper.setVisible(R.id.iv_right, false);
                helper.setText(R.id.type, item.getVpnName());
                helper.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_safety_shield));
                if (item.getAddressFrom().equals(currentWalletAddress)) {
                    //我消费
                    drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_falling);
                    helper.setText(R.id.shuzi, "-" + item.getQlc());
                } else {
                    drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_rise);
                    helper.setText(R.id.shuzi, "+" + item.getQlc());
                }
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvDesc.setCompoundDrawables(drawable, null, null, null);
                tvDesc.setCompoundDrawablePadding(20);
                tvDesc.setText("QLC");
                break;
            case 4:
                helper.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_green_logo));
                helper.setText(R.id.type, item.getWifiName());
                helper.setText(R.id.rate, "");
                helper.setVisible(R.id.ll_right, true);
                helper.setVisible(R.id.iv_right, false);
                helper.setText(R.id.shuzi, "-" + item.getQlc() + "");
                drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_falling);
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvDesc.setCompoundDrawables(drawable, null, null, null);
                tvDesc.setCompoundDrawablePadding(20);
                tvDesc.setText("QLC");
                break;
            case 5:
                helper.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_safety_shield));
                helper.setText(R.id.type, item.getVpnName());
                helper.setText(R.id.rate, "");
                helper.setVisible(R.id.ll_right, true);
                helper.setVisible(R.id.iv_right, false);
                helper.setText(R.id.shuzi, "-" + item.getQlc() + "");
                drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_falling);
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvDesc.setCompoundDrawables(drawable, null, null, null);
                tvDesc.setCompoundDrawablePadding(20);
                tvDesc.setText("QLC");
                break;
            case 6:
                helper.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_green_logo));
                helper.setText(R.id.type, item.getWifiName());
                helper.setText(R.id.rate, "");
                helper.setVisible(R.id.ll_right, true);
                helper.setVisible(R.id.iv_right, false);
                helper.setText(R.id.shuzi, "-" + item.getQlc() + "");
                drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_falling);
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvDesc.setCompoundDrawables(drawable, null, null, null);
                tvDesc.setCompoundDrawablePadding(20);
                tvDesc.setText("QLC");
                break;
            case 7:
                helper.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_safety_shield));
                helper.setText(R.id.type, item.getVpnName());
                helper.setText(R.id.rate, "");
                helper.setVisible(R.id.ll_right, true);
                helper.setVisible(R.id.iv_right, false);
                helper.setText(R.id.shuzi, "-" + item.getQlc() + "");
                drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_falling);
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvDesc.setCompoundDrawables(drawable, null, null, null);
                tvDesc.setCompoundDrawablePadding(20);
                tvDesc.setText("QLC");
                break;
            case 8:
                helper.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_purchase));
                helper.setText(R.id.type, "BNB Exchange QLC");
                helper.setText(R.id.rate, "");
                helper.setVisible(R.id.ll_right, true);
                helper.setVisible(R.id.iv_right, false);
                helper.setText(R.id.shuzi, "+" + item.getQlc() + "");
                drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_rise);
                /// 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvDesc.setCompoundDrawables(drawable, null, null, null);
                tvDesc.setCompoundDrawablePadding(20);
                tvDesc.setText("QLC");
                break;
            default:
                break;
        }
        helper.setText(R.id.time, item.getTime());
    }
}