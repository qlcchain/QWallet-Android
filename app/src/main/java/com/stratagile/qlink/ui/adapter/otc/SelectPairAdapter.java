package com.stratagile.qlink.ui.adapter.otc;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.otc.TradePair;

import java.util.ArrayList;
import java.util.List;

public class SelectPairAdapter extends BaseQuickAdapter<TradePair.PairsListBean, BaseViewHolder> {

    public SelectPairAdapter(@Nullable ArrayList<TradePair.PairsListBean> data) {
        super(R.layout.select_pair_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradePair.PairsListBean item) {
        helper.setText(R.id.tvTradePair, item.getTradeToken() + "/" + item.getPayToken());
    }
}
