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

public class NewRankListAdapter extends BaseQuickAdapter<ActiveList.DataBean.VpnRankingBean, BaseViewHolder> {
    private String currentActStatus = "";
    private int flagIndex = 0;

    public int getFlagIndex() {
        return flagIndex;
    }

    public void setFlagIndex(int flagIndex) {
        this.flagIndex = flagIndex;
    }

    public String getCurrentActStatus() {
        return currentActStatus;
    }

    public void setCurrentActStatus(String currentActStatus) {
        this.currentActStatus = currentActStatus;
    }

    public NewRankListAdapter(@Nullable List<ActiveList.DataBean.VpnRankingBean> data) {
        super(R.layout.item_new_rank_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActiveList.DataBean.VpnRankingBean item) {
        helper.setTextColor(R.id.tv_rank, mContext.getResources().getColor(R.color.color_333));
        helper.setTextColor(R.id.tv_connect_count, mContext.getResources().getColor(R.color.color_333));
        helper.setTextColor(R.id.tv_asset_name, mContext.getResources().getColor(R.color.color_333));
        if (item.getAssetName() == null || item.getAssetName().equals("")) {
            helper.setGone(R.id.ll_tip, true);
            helper.setGone(R.id.ll_content, false);
        } else {
            helper.setGone(R.id.ll_tip, false);
            helper.setGone(R.id.ll_content, true);
        }
        if (helper.getLayoutPosition() == 0) {
            helper.setVisible(R.id.line, false);
            helper.setGone(R.id.iv_one, true);
            helper.setGone(R.id.tv_rank, false);
            switch (getCurrentActStatus()) {
                case "":
                    helper.setGone(R.id.ll_prized, false);
                    break;
                case "NEW":
                    helper.setGone(R.id.ll_prized, false);
                    break;
                case "START":
//                    helper.setGone(R.id.ll_prized, true);
//                    helper.setText(R.id.tv_prized_number, "50%");
//                    helper.setText(R.id.tv_prized, mContext.getString(R.string.of_the_Price_Pool));
                    helper.setText(R.id.tv_connect_count, item.getTotalQlc() + "");
                    break;
                case "END":
//                    helper.setGone(R.id.ll_prized, true);
                    helper.setText(R.id.tv_connect_count, item.getRewardTotal() + "");
                    helper.setText(R.id.tv_prized, mContext.getString(R.string.Rewards));
                    break;
                case "PRIZED":
//                    helper.setGone(R.id.ll_prized, true);
                    helper.setText(R.id.tv_connect_count, item.getRewardTotal() + "");
                    helper.setText(R.id.tv_prized, mContext.getString(R.string.Rewards));
                    break;
                default:
                    break;
            }
        } else {
            helper.setVisible(R.id.line, true);
            helper.setGone(R.id.iv_one, false);
            helper.setGone(R.id.tv_rank, true);
            switch (getCurrentActStatus()) {
                case "":
                    helper.setGone(R.id.ll_prized, false);
                    break;
                case "NEW":
                    helper.setGone(R.id.ll_prized, false);
                    break;
                case "START":
                    helper.setGone(R.id.ll_prized, false);
                    if (helper.getLayoutPosition() >= flagIndex) {
                        helper.setVisible(R.id.tv_rank, false);
                    } else {
                        helper.setVisible(R.id.tv_rank, true);
                    }
                    helper.setText(R.id.tv_connect_count, item.getTotalQlc() + "");
                    break;
                case "END":
                    helper.setGone(R.id.ll_prized, true);
                    helper.setText(R.id.tv_connect_count, item.getRewardTotal() + "");
                    helper.setText(R.id.tv_prized, mContext.getString(R.string.Rewards));
                    break;
                case "PRIZED":
                    helper.setGone(R.id.ll_prized, true);
                    helper.setText(R.id.tv_connect_count, item.getRewardTotal() + "");
                    helper.setText(R.id.tv_prized, mContext.getString(R.string.Rewards));
//                    if (helper.getLayoutPosition() == mData.size() - 1) {
//                        helper.setTextColor(R.id.tv_rank, mContext.getResources().getColor(R.color.mainColor));
//                        helper.setTextColor(R.id.tv_connect_count, mContext.getResources().getColor(R.color.mainColor));
//                        helper.setTextColor(R.id.tv_asset_name, mContext.getResources().getColor(R.color.mainColor));
//                    } else {
//                        helper.setTextColor(R.id.tv_rank, mContext.getResources().getColor(R.color.color_333));
//                        helper.setTextColor(R.id.tv_connect_count, mContext.getResources().getColor(R.color.color_333));
//                        helper.setTextColor(R.id.tv_asset_name, mContext.getResources().getColor(R.color.color_333));
//                    }
                    break;
                default:
                    break;
            }
        }
        helper.setText(R.id.tv_rank, (helper.getLayoutPosition() + 1) + "");
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
