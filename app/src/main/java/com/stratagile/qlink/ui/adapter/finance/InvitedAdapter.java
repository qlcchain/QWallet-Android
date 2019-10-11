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
import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.entity.newwinq.Order;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.DateUtil;
import com.stratagile.qlink.utils.UserUtils;

import java.util.List;

public class InvitedAdapter extends BaseQuickAdapter<InviteList.Top5Bean, BaseViewHolder> {

    private float oneFriendReward;

    public InvitedAdapter(@Nullable List<InviteList.Top5Bean> data) {
        super(R.layout.item_invited_list, data);
    }

    public InvitedAdapter(@Nullable List<InviteList.Top5Bean> data, float oneFriendReward) {
        this(data);
        this.oneFriendReward = oneFriendReward;
    }


    @Override
    protected void convert(BaseViewHolder helper, InviteList.Top5Bean item) {
        helper.setText(R.id.userName, AccountUtil.setUserNickName(item.getName()));
        helper.setText(R.id.tvRank, item.getSequence() + "");
        helper.setText(R.id.invitePersons, item.getTotalInvite() * oneFriendReward + " QGAS");
        Glide.with(mContext)
                .load(MainAPI.MainBASE_URL + item.getHead())
                .apply(AppConfig.getInstance().options)
                .into((ImageView) helper.getView(R.id.userAvatar));
    }

}
