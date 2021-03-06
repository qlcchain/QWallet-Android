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
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.entity.otc.TradeOrderList;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.TimeUtil;

import java.math.BigDecimal;
import java.util.List;

public class TradeOrderAppealListAdapter extends BaseQuickAdapter<TradeOrderList.OrderListBean, BaseViewHolder> {

    public TradeOrderAppealListAdapter(@Nullable List<TradeOrderList.OrderListBean> data) {
        super(R.layout.trade_order_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradeOrderList.OrderListBean item) {
        helper.setText(R.id.tvPayToken, item.getPayToken());
        if (item.getBuyerId().equals(ConstantValue.currentUser.getUserId())) {
            //我买单
            helper.setText(R.id.tvOrderType, mContext.getString(R.string.buy) + " " + item.getTradeToken());
            helper.setTextColor(R.id.tvOrderType, mContext.getResources().getColor(R.color.mainColor));

        } else {
            helper.setText(R.id.tvOrderType, mContext.getString(R.string.sell) + " " + item.getTradeToken());
            helper.setTextColor(R.id.tvOrderType, mContext.getResources().getColor(R.color.color_ff3669));
        }
        switch (item.getAppealStatus()) {
            case "YES":
                helper.setText(R.id.tvOrderState, R.string.appeal_process);
                helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.color_ff3669));
                break;
            case "FAIL":
                helper.setText(R.id.tvOrderState, R.string.appeal_fail);
                helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.color_29282a));
                break;
            case "SUCCESS":
                helper.setText(R.id.tvOrderState, R.string.successful_appeal);
                helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.color_01b5ab));
                break;
            default:
                break;
        }
        helper.setText(R.id.tvAmountUsdt, BigDecimal.valueOf(item.getUsdtAmount()).stripTrailingZeros().toPlainString() + "");
        helper.setText(R.id.tvTime, TimeUtil.getOrderTime(TimeUtil.timeStamp(item.getCreateDate())));
        helper.setText(R.id.tvNickName, AccountUtil.setUserNickName(item.getNickname()));
        Glide.with(mContext)
                .load(MainAPI.MainBASE_URL + item.getHead())
                .apply(AppConfig.getInstance().options)
                .into((ImageView) helper.getView(R.id.ivAvatar));
    }
}
