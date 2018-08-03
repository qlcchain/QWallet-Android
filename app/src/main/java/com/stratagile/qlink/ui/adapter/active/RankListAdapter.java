package com.stratagile.qlink.ui.adapter.active;

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
import com.stratagile.qlink.entity.ActiveList;
import com.stratagile.qlink.utils.SpUtil;

import java.util.List;

public class RankListAdapter extends BaseQuickAdapter<ActiveList.DataBean.VpnRankingBean, BaseViewHolder> {

    public RankListAdapter(@Nullable List<ActiveList.DataBean.VpnRankingBean> data) {
        super(R.layout.item_rank_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActiveList.DataBean.VpnRankingBean item) {
        if (helper.getLayoutPosition() == 0) {
            helper.setVisible(R.id.line, false);
            helper.setGone(R.id.iv_one, true);
            helper.setGone(R.id.tv_rank, false);
        } else {
            helper.setVisible(R.id.line, true);
            helper.setGone(R.id.iv_one, false);
            helper.setGone(R.id.tv_rank, true);
        }
        helper.setText(R.id.tv_rank, (helper.getLayoutPosition() + 1) + "")
                .setText(R.id.tv_connect_count, item.getConnectSuccessNum() + "");
        helper.setText(R.id.tv_asset_name, item.getAssetName());
        ImageView imageView = helper.getView(R.id.iv_avatar);
        if (SpUtil.getBoolean(mContext, ConstantValue.isMainNet, false)) {
            Glide.with(mContext)
                    .load(MainAPI.MainBASE_URL + item.getImgUrl())
                    .apply(AppConfig.getInstance().optionsAvater)
                    .into(imageView);
        } else {
            Glide.with(mContext)
                    .load(API.BASE_URL + item.getImgUrl())
                    .apply(AppConfig.getInstance().optionsAvater)
                    .into(imageView);
        }
    }
}
