package com.stratagile.qlink.ui.adapter.vpn;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyleduo.switchbutton.SwitchButton;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.SpringAnimationUtil;
import com.stratagile.qlink.utils.ToastUtil;

import java.io.File;
import java.util.List;

/**
 * Created by huzhipeng on 2018/2/8.
 */

public class VpnListAdapter extends BaseQuickAdapter<VpnEntity, BaseViewHolder> {

    public VpnListAdapter(@Nullable List<VpnEntity> data) {
        super(R.layout.item_vpn_list, data);
    }

    private OnVpnOpreateListener onVpnOpreateListener;

    public OnVpnOpreateListener getOnVpnOpreateListener() {
        return onVpnOpreateListener;
    }

    public void setOnVpnOpreateListener(OnVpnOpreateListener onVpnOpreateListener) {
        this.onVpnOpreateListener = onVpnOpreateListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, VpnEntity item) {
        helper.setText(R.id.vpn_name, item.getVpnName());
        helper.addOnClickListener(R.id.freind_avater);
        helper.setText(R.id.connect_times, item.getConnsuccessNum() + "/times");
        helper.setText(R.id.price, item.getQlc() + " QLC/hour");
        helper.setText(R.id.tv_country, item.getCountry());
        ImageView avater = (ImageView) helper.getView(R.id.freind_avater);
        if (item.getAvatar() == null || item.getAvatar().equals("")) {
            if (item.getAvaterUpdateTime() != 0) {
                File imageFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + item.getAvaterUpdateTime() + ".jpg");
                if (imageFile.exists()) {
                    Glide.with(mContext)
                            .load(imageFile)
                            .apply(AppConfig.getInstance().optionsAvater)
                            .into(avater);
                } else {
                    avater.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_default_avatar));
                }
            } else {
                avater.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_default_avatar));
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
        if (item.isOnline()) {
            helper.setImageDrawable(R.id.friend_status, mContext.getResources().getDrawable(R.mipmap.icon_search));
            helper.setTextColor(R.id.vpn_name, mContext.getResources().getColor(R.color.color_333));
//            helper.setImageDrawable(R.id.iv_vpn_avater, mContext.getResources().getDrawable(R.mipmap.icon_choose_a_vpn));
            //我的vpn资产
            if (item.getP2pId().equals(SpUtil.getString(mContext, ConstantValue.P2PID, ""))) {
//                helper.setImageDrawable(R.id.iv_vpn_avater, mContext.getResources().getDrawable(R.mipmap.icon_my_vpn));
            }
            if (item.getUnReadMessageCount() != 0) {
                helper.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message_two));
                SpringAnimationUtil.startScaleSpringViewAnimation(helper.getView(R.id.message_status));
            } else {
                helper.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message));
            }
//            helper.setVisible(R.id.ll_content, true);
//            helper.setText(R.id.price, item.getQlc() + " QLC");
//            helper.setText(R.id.max_connect, item.getConnectMaxnumber() + "");
        } else {
//            avater.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_offline_default));
//            helper.setVisible(R.id.ll_content, false);
            helper.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message_three));
//            helper.setTextColor(R.id.vpn_name, mContext.getResources().getColor(R.color.color_a8a6a6));
            helper.setImageDrawable(R.id.friend_status, mContext.getResources().getDrawable(R.mipmap.icon_search_red));
//            helper.setImageDrawable(R.id.iv_vpn_avater, mContext.getResources().getDrawable(R.mipmap.icon_offiline_vpn));
        }
        SwitchButton switchButton = helper.getView(R.id.switchBar);
        switchButton.setEnabled(true);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!item.isOnline()) {
                        if (item.isConnected()) {
                            switchButton.setChecked(true);
                        } else {
                            switchButton.setChecked(false);
                            ToastUtil.displayShortToast(mContext.getResources().getString(R.string.please_wait_vpn_online));
                        }
                    } else {
                        if (onVpnOpreateListener != null && !item.isConnected()) {
                            onVpnOpreateListener.onConnect(item);
                            switchButton.setChecked(false);
                            switchButton.setEnabled(false);
                        }
                    }
                } else {
                    if (onVpnOpreateListener != null && item.isConnected()) {
                        onVpnOpreateListener.onDisConnect();
                    }
                }
            }
        });
        switchButton.setChecked(item.getIsConnected());
        //自己的资产不能抢注，别人的资产可以抢注，不在乎别人的资产是否在线
//        if (item.getP2pId().equals(SpUtil.getString(mContext, ConstantValue.P2PID, ""))) {
//            helper.setImageDrawable(R.id.iv_vpn_status, mContext.getResources().getDrawable(R.mipmap.icon_seize));
//        } else {
//            if(ConstantValue.isCloseRegisterAssetsInMain && SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false))
//            {
//                helper.setImageDrawable(R.id.iv_vpn_status, mContext.getResources().getDrawable(R.mipmap.icon_seize));
//            }else{
//                helper.setImageDrawable(R.id.iv_vpn_status, mContext.getResources().getDrawable(R.mipmap.icon_seize_two));
//            }
//
//        }
    }

    public interface OnVpnOpreateListener {
        void onConnect(VpnEntity vpnEntity);

        void onDisConnect();
    }
}
