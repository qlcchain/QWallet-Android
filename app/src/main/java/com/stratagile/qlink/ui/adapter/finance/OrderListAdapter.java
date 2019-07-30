package com.stratagile.qlink.ui.adapter.finance;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.newwinq.Order;
import com.stratagile.qlink.entity.newwinq.Product;
import com.stratagile.qlink.utils.DateUtil;
import com.stratagile.qlink.utils.LanguageUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderListAdapter extends BaseQuickAdapter<Order.OrderListBean, BaseViewHolder> {
    public OrderListAdapter(@Nullable List<Order.OrderListBean> data) {
        super(R.layout.item_order_list, data);
    }

    /**
     * "addRevenue" : 0.00,
     * "amount" : 1,
     * "productName" : "QLC锁仓30天",
     * "dueDays" : 0,   还剩多少天到期
     * "status" : "BUY"
     */

    @Override
    protected void convert(BaseViewHolder helper, Order.OrderListBean item) {
        helper.setText(R.id.tvPrincipal, item.getAmount() + "");
        helper.setText(R.id.tvCumulativeEarning, item.getAddRevenue() + "");
        helper.setText(R.id.tvMaturityDate, DateUtil.getMaturityDate(item.getDueDays(), mContext));
        helper.setGone(R.id.llNotReceived, false);
        helper.setGone(R.id.llRedeemed, false);
        if (!LanguageUtil.isCN(mContext)) {
            helper.setText(R.id.tvProductName, item.getProductNameEn());
        } else {
            helper.setText(R.id.tvProductName, item.getProductName());
        }
        if (item.getMaturityTime().equals("")) {
            //日日盈
            if (item.getStatus().equals("PAY")) {
                if (item.getDueDays() == 0) {
                    //可以赎回
                    helper.setVisible(R.id.llEarn, true);
                    helper.setGone(R.id.llMaturityTime, false);
                    helper.setGone(R.id.llRedeem, true);
                    helper.addOnClickListener(R.id.llRedeem);
                } else {
                    //不可以赎回
                    helper.setVisible(R.id.llEarn, true);
                    helper.setGone(R.id.llMaturityTime, true);
                    helper.setGone(R.id.llRedeem, false);
                }
            } else if (item.getStatus().equals("BUY")) {
                helper.setVisible(R.id.llEarn, false);
                helper.setGone(R.id.llMaturityTime, false);
                helper.setGone(R.id.llRedeem, false);
                helper.setGone(R.id.llNotReceived, true);
            } else if (item.getStatus().equals("REDEEM")) {
                //已经赎回
                helper.setVisible(R.id.llEarn, false);
                helper.setGone(R.id.llMaturityTime, false);
                helper.setGone(R.id.llRedeem, false);
                helper.setGone(R.id.llNotReceived, false);
                helper.setVisible(R.id.llRedeemed, true);
            }
        } else {
            //正常的订单
            if (item.getStatus().equals("END")) {
                //可以赎回
                helper.setVisible(R.id.llEarn, true);
                helper.setGone(R.id.llMaturityTime, false);
                helper.setGone(R.id.llRedeem, true);
                helper.addOnClickListener(R.id.llRedeem);
            } else if (item.getStatus().equals("PAY")) {
                //正在抵押的订单,不可以赎回
                helper.setVisible(R.id.llEarn, true);
                helper.setGone(R.id.llMaturityTime, true);
                helper.setGone(R.id.llRedeem, false);
            } else if (item.getStatus().equals("BUY")) {
                helper.setVisible(R.id.llEarn, false);
                helper.setGone(R.id.llMaturityTime, false);
                helper.setGone(R.id.llRedeem, false);
                helper.setGone(R.id.llNotReceived, true);
            } else if (item.getStatus().equals("REDEEM")) {
                helper.setVisible(R.id.llEarn, false);
                helper.setGone(R.id.llMaturityTime, false);
                helper.setGone(R.id.llRedeem, false);
                helper.setGone(R.id.llNotReceived, false);
                helper.setVisible(R.id.llRedeemed, true);
            }
        }
    }

}
