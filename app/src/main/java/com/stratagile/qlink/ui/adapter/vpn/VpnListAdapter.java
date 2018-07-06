package com.stratagile.qlink.ui.adapter.vpn;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.SpringAnimationUtil;

import java.io.File;
import java.util.List;

/**
 * Created by huzhipeng on 2018/2/8.
 */

public class VpnListAdapter extends BaseQuickAdapter<VpnEntity, BaseViewHolder> {

    public VpnListAdapter(@Nullable List<VpnEntity> data) {
        super(R.layout.item_vpn_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VpnEntity item) {
        helper.setText(R.id.vpn_name, item.getVpnName());
        CardView cardView = helper.getView(R.id.cardView);
        helper.addOnClickListener(R.id.freind_avater);
        helper.setTextColor(R.id.vpn_name, mContext.getResources().getColor(R.color.mainColor));
        helper.setTextColor(R.id.price, mContext.getResources().getColor(R.color.color_333));
        helper.setTextColor(R.id.max_connect, mContext.getResources().getColor(R.color.color_333));
        cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
        helper.addOnClickListener(R.id.iv_vpn_status);
        ImageView avater = (ImageView) helper.getView(R.id.freind_avater);
        if (item.getAvatar() == null || item.getAvatar().equals("")) {
            if (item.getAvaterUpdateTime() != 0) {
                File imageFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + item.getAvaterUpdateTime() + ".jpg");
                if (imageFile.exists()) {
                    if (item.getIsConnected()) {
                        Glide.with(mContext)
                                .load(imageFile)
                                .apply(AppConfig.getInstance().options)
                                .into(avater);
                    } else {
                        Glide.with(mContext)
                                .load(imageFile)
                                .apply(AppConfig.getInstance().optionsAvater)
                                .into(avater);
                    }
                } else {
                    avater.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_default_avatar));
//                    if (ConstantValue.myStatus > 0 && item.isOnline()) {
//                        if (ConstantValue.loadingAvatarList.contains(item.getAvaterUpdateTime() + "")) {
//                            //已经对改头像请求了加载
//                        } else {
//                            if (ConstantValue.isLoadingImg) {
//                                return;
//                            }
//                            KLog.i("好友的头像不存在，请求发送图片过来");
//                            if (Qsdk.getInstance().addFileSendRequest(item.getFriendNum()) == 0) {
//                                ConstantValue.loadingAvatarList.add(item.getAvaterUpdateTime() + "");
//                            }
//                        }
//                    } else {
////                    KLog.i("没有登录，或者过滤掉重复的头像请求");
//                    }
                }
            } else {
                avater.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_default_avatar));
            }
        } else {
            if (item.getIsConnected()) {
                if (SpUtil.getBoolean(mContext, ConstantValue.isMainNet, false)) {
                    Glide.with(mContext)
                            .load(MainAPI.MainBASE_URL + item.getAvatar().replace("\\", "/"))
                            .apply(AppConfig.getInstance().options)
                            .into(avater);
                } else {
                    Glide.with(mContext)
                            .load(API.BASE_URL + item.getAvatar().replace("\\", "/"))
                            .apply(AppConfig.getInstance().options)
                            .into(avater);
                }

            } else {
                if (SpUtil.getBoolean(mContext, ConstantValue.isMainNet, false)) {
                    Glide.with(mContext)
                            .load(MainAPI.MainBASE_URL + item.getAvatar().replace("\\", "/"))
                            .apply(AppConfig.getInstance().optionsAvater)
                            .into(avater);
                } else {
                    Glide.with(mContext)
                            .load(API.BASE_URL + item.getAvatar().replace("\\", "/"))
                            .apply(AppConfig.getInstance().optionsAvater)
                            .into(avater);
                }
            }
        }
        if (item.isOnline()) {
            helper.setImageDrawable(R.id.friend_status, mContext.getResources().getDrawable(R.mipmap.icon_search));
            helper.setTextColor(R.id.vpn_name, mContext.getResources().getColor(R.color.color_333));
            helper.setImageDrawable(R.id.iv_vpn_avater, mContext.getResources().getDrawable(R.mipmap.icon_choose_a_vpn));
            //我的vpn资产
            if (item.getP2pId().equals(SpUtil.getString(mContext, ConstantValue.P2PID, ""))) {
                helper.setImageDrawable(R.id.iv_vpn_avater, mContext.getResources().getDrawable(R.mipmap.icon_my_vpn));
            }
            if (item.getUnReadMessageCount() != 0) {
                helper.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message_two));
                SpringAnimationUtil.startScaleSpringViewAnimation(helper.getView(R.id.message_status));
            } else {
                helper.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message));
            }
            helper.setVisible(R.id.ll_content, true);
            helper.setText(R.id.price, item.getQlc() + " QLC");
            helper.setText(R.id.max_connect, item.getConnectMaxnumber() + "");
        } else {
//            avater.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_offline_default));
            helper.setVisible(R.id.ll_content, false);
            helper.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message_three));
            helper.setTextColor(R.id.vpn_name, mContext.getResources().getColor(R.color.color_a8a6a6));
            helper.setImageDrawable(R.id.friend_status, mContext.getResources().getDrawable(R.mipmap.icon_search_red));
            helper.setImageDrawable(R.id.iv_vpn_avater, mContext.getResources().getDrawable(R.mipmap.icon_offiline_vpn));
        }
        if (item.getIsConnected()) {
            cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.mainColor));
            helper.setTextColor(R.id.vpn_name, mContext.getResources().getColor(R.color.white));
            helper.setTextColor(R.id.price, mContext.getResources().getColor(R.color.white));
            helper.setTextColor(R.id.max_connect, mContext.getResources().getColor(R.color.white));
            helper.setImageDrawable(R.id.iv_vpn_avater, mContext.getResources().getDrawable(R.mipmap.icon_my_vpn));
        } else {

        }
        //自己的资产不能抢注，别人的资产可以抢注，不在乎别人的资产是否在线
        if (item.getP2pId().equals(SpUtil.getString(mContext, ConstantValue.P2PID, ""))) {
            helper.setImageDrawable(R.id.iv_vpn_status, mContext.getResources().getDrawable(R.mipmap.icon_seize));
        } else {
            if(ConstantValue.isCloseRegisterAssetsInMain && SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false))
            {
                helper.setImageDrawable(R.id.iv_vpn_status, mContext.getResources().getDrawable(R.mipmap.icon_seize));
            }else{
                helper.setImageDrawable(R.id.iv_vpn_status, mContext.getResources().getDrawable(R.mipmap.icon_seize_two));
            }

        }
    }
}
