package com.stratagile.qlink.ui.adapter.finance;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.entity.finance.EarnRank;
import com.stratagile.qlink.entity.finance.MyRanking;

import java.util.List;

public class EarnRankAdapter extends BaseQuickAdapter<EarnRank.DataBean, BaseViewHolder> {

    public EarnRankAdapter(@Nullable List<EarnRank.DataBean> data) {
        super(R.layout.item_earnrank_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EarnRank.DataBean item) {
        helper.setText(R.id.userName, item.getName());
        helper.setText(R.id.tvRank, item.getSequence() + "");
        helper.setText(R.id.earnQlc, "+" + item.getTotalRevenue() + " QLC");
        Glide.with(mContext)
                .load(MainAPI.MainBASE_URL + item.getHead())
                .apply(AppConfig.getInstance().options)
                .into((ImageView) helper.getView(R.id.userAvatar));
    }
}
