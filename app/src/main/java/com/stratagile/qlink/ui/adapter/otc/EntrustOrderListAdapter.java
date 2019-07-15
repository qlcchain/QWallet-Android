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

import java.math.BigDecimal;
import java.util.List;

public class EntrustOrderListAdapter extends BaseQuickAdapter<EntrustOrderList.OrderListBean, BaseViewHolder> {

    public EntrustOrderListAdapter(@Nullable List<EntrustOrderList.OrderListBean> data) {
        super(R.layout.entrust_order_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntrustOrderList.OrderListBean item) {
        helper.setText(R.id.tvUnitPrice, BigDecimal.valueOf(item.getUnitPrice()).stripTrailingZeros().toPlainString());
        if (item.getType().equals(ConstantValue.orderTypeBuy)) {
            helper.setTextColor(R.id.tvUnitPrice, mContext.getResources().getColor(R.color.color_108ee9));
        } else {
            helper.setTextColor(R.id.tvUnitPrice, mContext.getResources().getColor(R.color.color_ff3669));
        }
        helper.setText(R.id.tvNickName, item.getNickname());
        helper.setText(R.id.tvAmount, BigDecimal.valueOf(item.getTotalAmount()).stripTrailingZeros().toPlainString() + "");
        helper.setText(R.id.tvQgasVolume, BigDecimal.valueOf(item.getMinAmount()).stripTrailingZeros().toPlainString() + "-" + BigDecimal.valueOf(item.getMaxAmount()).stripTrailingZeros().toPlainString());
        Glide.with(mContext)
                .load(API.BASE_URL + item.getHead())
                .apply(AppConfig.getInstance().options)
                .into((ImageView) helper.getView(R.id.ivAvatar));
    }
}
