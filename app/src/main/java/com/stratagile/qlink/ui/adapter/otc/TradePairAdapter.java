package com.stratagile.qlink.ui.adapter.otc;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.entity.otc.TradePair;

import java.util.List;

public class TradePairAdapter extends BaseQuickAdapter<TradePair.PairsListBean, BaseViewHolder> {

    public TradePairAdapter(@Nullable List<TradePair.PairsListBean> data) {
        super(R.layout.trade_pair_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradePair.PairsListBean item) {
        helper.setText(R.id.tvTradePair, item.getTradeToken() + "/" + item.getPayToken());
        if (item.isSelect()) {
            helper.setBackgroundRes(R.id.tvTradePair, R.drawable.trade_pair_select_bg);
            helper.setTextColor(R.id.tvTradePair, mContext.getResources().getColor(R.color.mainColor));
        } else {
            helper.setBackgroundRes(R.id.tvTradePair, R.drawable.trade_pair_unselect_bg);
            helper.setTextColor(R.id.tvTradePair, mContext.getResources().getColor(R.color.color_29282a));
        }
    }
}
