package com.stratagile.qlink.ui.adapter.finance;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.entity.finance.MyRanking;
import com.stratagile.qlink.utils.AccountUtil;

import java.util.List;

public class MyRankAdapter extends BaseQuickAdapter<MyRanking.DataBean, BaseViewHolder> {

    public MyRankAdapter(@Nullable List<MyRanking.DataBean> data) {
        super(R.layout.item_invited_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyRanking.DataBean item) {
        helper.setText(R.id.userName, AccountUtil.setUserNickName(item.getName()));
        helper.setText(R.id.tvRank, item.getSequence() + "");
        helper.setText(R.id.invitePersons, mContext.getString(R.string.invited_) + " " + item.getTotalInvite() + " " +  mContext.getString(R.string.friends));
        Glide.with(mContext)
                .load(MainAPI.MainBASE_URL + item.getHead())
                .apply(AppConfig.getInstance().options)
                .into((ImageView) helper.getView(R.id.userAvatar));
    }
}
