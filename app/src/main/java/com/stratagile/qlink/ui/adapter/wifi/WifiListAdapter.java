package com.stratagile.qlink.ui.adapter.wifi;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.utils.SpUtil;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.utils.SpringAnimationUtil;

import java.io.File;
import java.util.List;


/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: $description  adapter of WifiFragment
 * @date 2018/01/09 14:02:09
 */

public class WifiListAdapter extends BaseQuickAdapter<WifiEntity, BaseViewHolder> {

    public WifiListAdapter(@Nullable List<WifiEntity> data) {
        super(R.layout.item_wifi_list, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WifiEntity item) {
        baseViewHolder.setText(R.id.adapter_wifi_TextViewSSID, item.getSsid() == null ? "  " : item.getSsid());
        CardView cardView = baseViewHolder.getView(R.id.adapter_wifi_LinearLayoutContainer);
        baseViewHolder.addOnClickListener(R.id.freind_avater);
        cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
        baseViewHolder.addOnClickListener(R.id.iv_wifi_status);
        baseViewHolder.setTextColor(R.id.adapter_wifi_TextViewSSID, mContext.getResources().getColor(R.color.color_333));
        baseViewHolder.setTextColor(R.id.tv_qlc, mContext.getResources().getColor(R.color.color_333));
        baseViewHolder.setTextColor(R.id.allow, mContext.getResources().getColor(R.color.color_333));
        baseViewHolder.setText(R.id.allow, item.getDeviceAllowed() - item.getConnectCount() > 0 ? item.getDeviceAllowed() - item.getConnectCount() + "" : 0 + "");
        if (item.isRegiste()) {
            baseViewHolder.setVisible(R.id.rl_friend, true);
            baseViewHolder.setVisible(R.id.allow, true);
            baseViewHolder.setVisible(R.id.iv_wifi_status, true);
            baseViewHolder.setVisible(R.id.ll_status, true);
            ImageView avater = (ImageView) baseViewHolder.getView(R.id.freind_avater);
            if (item.getAvatar() == null || item.getAvatar().equals("")) {
                if (item.getAvaterUpdateTime() != 0) {
                    File imageFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + item.getAvaterUpdateTime() + ".jpg");
                    if (item.getOwnerP2PId().equals(SpUtil.getString(mContext, ConstantValue.P2PID, ""))) {
//                KLog.i("设置自己的头像");
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
//                        if (!imageFile.exists() && !item.getIsLoadingAvater() && ConstantValue.myStatus > 0 && item.isOnline()) {
//                            KLog.i("好友的头像不存在，请求发送图片过来");
//                            Qsdk.getInstance().addFileSendRequest(item.getFreindNum());
//                            item.setIsLoadingAvater(true);
//                            AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(item);
//                        } else {
////                    KLog.i("没有登录，或者过滤掉重复的头像请求");
//                        }
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
                            KLog.i("给这个wifi设置默认头像");
                            KLog.i(item.toString());
                            Glide.with(mContext)
                                    .load(R.mipmap.img_default_avatar)
                                    .apply(AppConfig.getInstance().optionsAvater)
                                    .into(avater);
                        }
                    }
                } else {
                    Glide.with(mContext)
                            .load(R.mipmap.img_default_avatar)
                            .apply(AppConfig.getInstance().optionsAvater)
                            .into(avater);
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
                baseViewHolder.setImageDrawable(R.id.friend_status, mContext.getResources().getDrawable(R.mipmap.icon_search));
                if (item.getConnectCount() != 0) {
                }
            } else {
//                baseViewHolder.setText(R.id.tv_qlc, "");
//                baseViewHolder.setVisible(R.id.tv_qlc_desc, false);
                baseViewHolder.setImageDrawable(R.id.friend_status, mContext.getResources().getDrawable(R.mipmap.icon_search_red));
            }
            baseViewHolder.setVisible(R.id.tv_qlc_desc, true);
            baseViewHolder.setText(R.id.tv_qlc, item.getPriceInQlc() + " QLC");
        } else {
            baseViewHolder.setVisible(R.id.rl_friend, false);
            baseViewHolder.setVisible(R.id.allow, false);
            baseViewHolder.setText(R.id.tv_qlc, "");
            baseViewHolder.setVisible(R.id.tv_qlc_desc, false);
            baseViewHolder.setVisible(R.id.iv_wifi_status, false);
            baseViewHolder.setVisible(R.id.ll_status, false);
        }
        if (item.isOnline()) {
            if (item.getUnReadMessageCount() != 0) {
                baseViewHolder.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message_two));
                SpringAnimationUtil.startScaleSpringViewAnimation(baseViewHolder.getView(R.id.message_status));
            } else {
                baseViewHolder.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message));
            }
            switch (item.getLevel()) {
                case 0:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_purple_one));
                    break;
                case 1:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_purple_two));
                    break;
                case 2:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_wifi_normal));
                    break;
                case 3:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_wifi_normal));
                    break;
                default:
                    break;
            }
        } else {
            baseViewHolder.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message_three));
            //baseViewHolder.setImageDrawable(R.id.iv_wifi_status, mContext.getResources().getDrawable(R.mipmap.icon_wifi_action_three));
            switch (item.getLevel()) {
                case 0:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_red_one));
                    break;
                case 1:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_red_two));
                    break;
                case 2:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_red));
                    break;
                case 3:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_red));
                    break;
                default:
                    break;
            }
        }
        if (item.getIsConnected()) {
            cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.mainColor));
            baseViewHolder.setTextColor(R.id.adapter_wifi_TextViewSSID, mContext.getResources().getColor(R.color.white));
            baseViewHolder.setTextColor(R.id.tv_qlc, mContext.getResources().getColor(R.color.white));
            baseViewHolder.setTextColor(R.id.allow, mContext.getResources().getColor(R.color.white));
            switch (item.getLevel()) {
                case 0:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_green_one));
                    break;
                case 1:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_green_two));
                    break;
                case 2:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_wifi_online));
                    break;
                case 3:
                    baseViewHolder.setImageDrawable(R.id.iv_wifi_avater, mContext.getResources().getDrawable(R.mipmap.icon_wifi_online));
                    break;
                default:
                    break;
            }
        } else {
            //baseViewHolder.setImageDrawable(R.id.iv_wifi_status, mContext.getResources().getDrawable(R.mipmap.icon_wifi_action));
        }
        if (item.getOwnerP2PId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
            baseViewHolder.setImageDrawable(R.id.iv_wifi_status, mContext.getResources().getDrawable(R.mipmap.icon_seize));
        } else {
            if(ConstantValue.isCloseRegisterAssetsInMain && SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false))
            {
                baseViewHolder.setImageDrawable(R.id.iv_wifi_status, mContext.getResources().getDrawable(R.mipmap.icon_seize));
            }else{
                baseViewHolder.setImageDrawable(R.id.iv_wifi_status, mContext.getResources().getDrawable(R.mipmap.icon_seize_two));
            }

        }
    }
}