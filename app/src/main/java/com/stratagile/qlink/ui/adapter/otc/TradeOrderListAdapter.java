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

import java.math.BigDecimal;
import java.util.List;

public class TradeOrderListAdapter extends BaseQuickAdapter<TradeOrderList.OrderListBean, BaseViewHolder> {

    public TradeOrderListAdapter(@Nullable List<TradeOrderList.OrderListBean> data) {
        super(R.layout.trade_order_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradeOrderList.OrderListBean item) {
        if (item.getBuyerId().equals(ConstantValue.currentUser.getUserId())) {
            //买单
            helper.setText(R.id.tvOrderType, "买入");
            helper.setTextColor(R.id.tvOrderType, mContext.getResources().getColor(R.color.mainColor));
            switch (item.getStatus()) {
                case "QGAS_TO_PLATFORM":
                    helper.setText(R.id.tvOrderState, "待付款");
                    helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.color_ff3669));
                    break;
                case "OVERTIME":
                    helper.setText(R.id.tvOrderState, "已超时");
                    helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.color_ff3669));
                    break;
                default:
                    break;
            }
        } else {
            helper.setText(R.id.tvOrderType, "卖出");
            helper.setTextColor(R.id.tvOrderType, mContext.getResources().getColor(R.color.color_ff3669));
            switch (item.getStatus()) {
                case "QGAS_TO_PLATFORM":
                    helper.setText(R.id.tvOrderState, "等待对方付款");
                    helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.mainColor));
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
//        Glide.with(mContext)
//                .load(API.BASE_URL + item.getHead())
//                .apply(AppConfig.getInstance().options)
//                .into((ImageView) helper.getView(R.id.ivAvatar));
    }
}
