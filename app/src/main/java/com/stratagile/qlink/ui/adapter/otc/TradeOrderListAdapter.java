package com.stratagile.qlink.ui.adapter.otc;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.entity.EntrustOrderList;
import com.stratagile.qlink.entity.otc.TradeOrderList;
import com.stratagile.qlink.utils.TimeUtil;

import java.math.BigDecimal;
import java.util.List;

public class TradeOrderListAdapter extends BaseQuickAdapter<TradeOrderList.OrderListBean, BaseViewHolder> {

    public TradeOrderListAdapter(@Nullable List<TradeOrderList.OrderListBean> data) {
        super(R.layout.trade_order_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradeOrderList.OrderListBean item) {
        if (item.getBuyerId().equals(ConstantValue.currentUser.getUserId())) {
            //我买单
            helper.setText(R.id.tvOrderType, "BUY QGAS");
            helper.setTextColor(R.id.tvOrderType, mContext.getResources().getColor(R.color.mainColor));
            switch (item.getStatus()) {
                case "QGAS_TO_PLATFORM":
                    helper.setText(R.id.tvOrderState, "待付款");
                    helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.color_ff3669));
                    break;
                case "USDT_PAID":
                    helper.setText(R.id.tvOrderState, "等待对方确认收款");
                    helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.mainColor));
                    break;
                case "OVERTIME":
                    helper.setText(R.id.tvOrderState, "已超时");
                    helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.color_ff3669));
                    break;
                default:
                    break;
            }
        } else {
            helper.setText(R.id.tvOrderType, "SELL QGAS");
            helper.setTextColor(R.id.tvOrderType, mContext.getResources().getColor(R.color.color_ff3669));
            switch (item.getStatus()) {
                case "QGAS_TO_PLATFORM":
                    helper.setText(R.id.tvOrderState, "等待对方付款");
                    helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.mainColor));
                    break;
                case "USDT_PAID":
                    helper.setText(R.id.tvOrderState, "等待确认查收");
                    helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.color_ff3669));
                    break;
                case "OVERTIME":
                    helper.setText(R.id.tvOrderState, "已超时");
                    helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.color_ff3669));
                    break;
                default:
                    break;
            }
        }
        helper.setText(R.id.tvAmountUsdt, BigDecimal.valueOf(item.getUsdtAmount()).stripTrailingZeros().toPlainString() + "");
        helper.setText(R.id.tvTime, TimeUtil.getOrderTime(TimeUtil.timeStamp(item.getCreateDate())));
        helper.setText(R.id.tvNickName, item.getNickname());
        Glide.with(mContext)
                .load(API.BASE_URL + item.getHead())
                .apply(AppConfig.getInstance().options)
                .into((ImageView) helper.getView(R.id.ivAvatar));
    }
}
