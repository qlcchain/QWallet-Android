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
import com.stratagile.qlink.utils.TimeUtil;

import java.math.BigDecimal;
import java.util.List;

public class EntrustPostedOrderListAdapter extends BaseQuickAdapter<EntrustOrderList.OrderListBean, BaseViewHolder> {

    public EntrustPostedOrderListAdapter(@Nullable List<EntrustOrderList.OrderListBean> data) {
        super(R.layout.entrust_posted_order_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntrustOrderList.OrderListBean item) {
        helper.setText(R.id.tvUnitPrice, BigDecimal.valueOf(item.getUnitPrice()).stripTrailingZeros().toPlainString());
        if (item.getType().equals(ConstantValue.orderTypeBuy)) {
            helper.setText(R.id.tvOrderType, R.string.buy_qgas);
            helper.setTextColor(R.id.tvOrderType, mContext.getResources().getColor(R.color.mainColor));
        } else {
            helper.setText(R.id.tvOrderType, R.string.sell_qgas);
            helper.setTextColor(R.id.tvOrderType, mContext.getResources().getColor(R.color.color_ff3669));
        }
        switch (item.getStatus()) {
            case "NORMAL":
                helper.setText(R.id.tvOrderState, R.string.active);
                helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.mainColor));
                break;
            case "END":
                helper.setText(R.id.tvOrderState, R.string.completed);
                helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.color_21beb5));
                break;
            case "CANCEL":
                helper.setText(R.id.tvOrderState, R.string.revoked);
                helper.setTextColor(R.id.tvOrderState, mContext.getResources().getColor(R.color.color_29282a));
                break;
            default:
                break;
        }
        helper.setText(R.id.tvUnitPrice, BigDecimal.valueOf(item.getUnitPrice()).stripTrailingZeros().toPlainString() + "");
        helper.setText(R.id.tvDeals, TimeUtil.getOrderTime(TimeUtil.timeStamp(item.getOrderTime())));
        helper.setText(R.id.tvNickName, item.getNickname());
        helper.setText(R.id.tvAmount, BigDecimal.valueOf(item.getTotalAmount()).stripTrailingZeros().toPlainString() + "");
        helper.setText(R.id.tvQgasVolume, BigDecimal.valueOf(item.getMinAmount()).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(item.getMaxAmount()).stripTrailingZeros().toPlainString());
        Glide.with(mContext)
                .load(API.BASE_URL + item.getHead())
                .apply(AppConfig.getInstance().options)
                .into((ImageView) helper.getView(R.id.ivAvatar));
    }
}
