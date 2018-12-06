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
        helper.setText(R.id.connect_times, item.getConnsuccessNum() + "");
        helper.setText(R.id.price, item.getQlc() + "/hour");
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
        helper.setGone(R.id.viewOnline, !item.isOnline());
        helper.setVisible(R.id.switchBar, item.isOnline());
        if (item.isOnline()) {
            helper.setImageDrawable(R.id.friend_status, mContext.getResources().getDrawable(R.mipmap.icon_search));
            helper.setTextColor(R.id.vpn_name, mContext.getResources().getColor(R.color.color_333));
            //我的vpn资产
            if (item.getP2pId().equals(SpUtil.getString(mContext, ConstantValue.P2PID, ""))) {
            }
            if (item.getUnReadMessageCount() != 0) {
                helper.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message_two));
                SpringAnimationUtil.startScaleSpringViewAnimation(helper.getView(R.id.message_status));
            } else {
                helper.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message));
            }
        } else {
            helper.setImageDrawable(R.id.message_status, mContext.getResources().getDrawable(R.mipmap.icon_owner_message_three));
            helper.setImageDrawable(R.id.friend_status, mContext.getResources().getDrawable(R.mipmap.icon_search_red));
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
    }

    public interface OnVpnOpreateListener {
        void onConnect(VpnEntity vpnEntity);

        void onDisConnect();
    }
}
