package com.stratagile.qlink.ui.adapter.finance;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.newwinq.Product;

import java.util.List;

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
        helper.setText(R.id.tvProductName, item.getName());
        helper.setText(R.id.tvProfit, item.getAnnualIncomeRate() * 100 + "%");
        helper.setText(R.id.tvQlcCount, "From " + item.getLeastAmount() + " QLC");
        helper.setText(R.id.tvDayTime, item.getTimeLimit() + "");
    }
}
