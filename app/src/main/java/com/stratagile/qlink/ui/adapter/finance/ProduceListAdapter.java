package com.stratagile.qlink.ui.adapter.finance;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.newwinq.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class ProduceListAdapter extends BaseQuickAdapter<Product.DataBean, BaseViewHolder> {
    public ProduceListAdapter(@Nullable List<Product.DataBean> data) {
        super(R.layout.item_product_list, data);
    }

    /**
     * timeLimit : 30
     * annualIncomeRate : 0.2
     * name : QLC锁仓30天
     * id : a721b33c572742d097e36c75f0089ce4
     * leastAmount : 100
     * status : ON_SALE
     */

    @Override
    protected void convert(BaseViewHolder helper, Product.DataBean item) {
        helper.setText(R.id.tvProfit, BigDecimal.valueOf(item.getAnnualIncomeRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
        helper.setText(R.id.tvQlcCount, mContext.getResources().getString(R.string.from_) + " " + item.getLeastAmount() + " QLC");
        helper.setText(R.id.tvDayTime, item.getTimeLimit() + "");
        if (mContext.getResources().getConfiguration().locale == Locale.ENGLISH) {
            helper.setText(R.id.tvProductName, item.getNameEn());
        } else {
            helper.setText(R.id.tvProductName, item.getName());
        }
        if (item.getStatus().equals("END")) {
            helper.setTextColor(R.id.tvProductName, mContext.getResources().getColor(R.color.color_999));
            helper.setVisible(R.id.soldOut, true);
            helper.setTextColor(R.id.tvProfit, mContext.getResources().getColor(R.color.color_999));
            helper.setTextColor(R.id.tvDayTime, mContext.getResources().getColor(R.color.color_999));
            helper.setTextColor(R.id.tvDays, mContext.getResources().getColor(R.color.color_999));
        } else {
            helper.setVisible(R.id.soldOut, false);
            helper.setTextColor(R.id.tvProductName, mContext.getResources().getColor(R.color.color_4b494d));
            helper.setTextColor(R.id.tvProfit, mContext.getResources().getColor(R.color.color_01b5ab));
            helper.setTextColor(R.id.tvDayTime, mContext.getResources().getColor(R.color.color_29282a));
            helper.setTextColor(R.id.tvDays, mContext.getResources().getColor(R.color.color_29282a));
        }
    }
}
