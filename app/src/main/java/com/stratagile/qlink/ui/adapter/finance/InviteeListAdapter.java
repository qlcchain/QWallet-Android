package com.stratagile.qlink.ui.adapter.finance;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.entity.reward.InviteeList;
import com.stratagile.qlink.utils.AccountUtil;

import java.util.List;

public class InviteeListAdapter extends BaseQuickAdapter<InviteeList.InviteeListBean, BaseViewHolder> {


    public InviteeListAdapter(@Nullable List<InviteeList.InviteeListBean> data) {
        super(R.layout.item_invitee_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InviteeList.InviteeListBean item) {
        helper.setText(R.id.userName, AccountUtil.setUserNickName(item.getNickname()));
        if ("AWARDED".equals(item.getStatus())) {
            helper.setText(R.id.inviteStatus, mContext.getString(R.string.referred));
        } else {
            helper.setText(R.id.inviteStatus, mContext.getString(R.string.no_referred));
        }
        Glide.with(mContext)
                .load(MainAPI.MainBASE_URL + item.getHead())
                .apply(AppConfig.getInstance().options)
                .into((ImageView) helper.getView(R.id.userAvatar));
    }

}
