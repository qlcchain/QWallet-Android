package com.stratagile.qlink.ui.adapter.im;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.im.Message;
import com.stratagile.qlink.utils.SpUtil;

import java.io.File;
import java.util.List;

/**
 * Created by huzhipeng on 2018/3/20.
 */

public class ConversationListAdapter extends BaseQuickAdapter<Message, BaseViewHolder> {

//    private final File dataFile;
    private VpnEntity vpnEntity;

    private WifiEntity wifiEntity;

    public WifiEntity getWifiEntity() {
        return wifiEntity;
    }

    public void setWifiEntity(WifiEntity wifiEntity) {
        this.wifiEntity = wifiEntity;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    private int chatType;

    public VpnEntity getVpnEntity() {
        return vpnEntity;
    }

    public void setVpnEntity(VpnEntity vpnEntity) {
        this.vpnEntity = vpnEntity;
    }



    public ConversationListAdapter(@Nullable List<Message> data) {
        super(R.layout.item_conversation_list, data);
//        dataFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + SpUtil.getString(mContext, ConstantValue.myAvaterUpdateTime, "") + ".jpg", "");
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        helper.setText(R.id.tv_name, item.getNickName());
        helper.setText(R.id.tv_content, item.getContent());
        ImageView ivRight = helper.getView(R.id.iv_right);
        ImageView ivLeft = helper.getView(R.id.iv_left);
        RelativeLayout rlContent = helper.getView(R.id.rl_content);
        //1代表自己
        if (item.getDirection() == 1) {
            ivRight.setVisibility(View.VISIBLE);
            ivLeft.setVisibility(View.INVISIBLE);
            helper.setTextColor(R.id.tv_content, mContext.getResources().getColor(R.color.white));
            helper.setTextColor(R.id.tv_name, mContext.getResources().getColor(R.color.white));
//            rlContent.setGravity(Gravity.RIGHT);
            FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
            rlContent.setLayoutParams(fl);
            rlContent.setBackgroundResource(R.mipmap.rc_ic_bubble_no_right);
            Glide.with(mContext)
                    .load(API.BASE_URL + SpUtil.getString(mContext, ConstantValue.myAvatarPath, ""))
                    .apply(AppConfig.getInstance().optionsMainColor)
                    .into(ivRight);
        } else {
            rlContent.setBackgroundResource(R.mipmap.rc_ic_bubble_left);
            FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.LEFT);
            rlContent.setLayoutParams(fl);
            helper.setTextColor(R.id.tv_content, mContext.getResources().getColor(R.color.color_333));
            helper.setTextColor(R.id.tv_name, mContext.getResources().getColor(R.color.color_333));
            ivRight.setVisibility(View.INVISIBLE);
            ivLeft.setVisibility(View.VISIBLE);
            if (item.getAvatar() == null || item.getAvatar().equals("")) {
                File dataFile1 = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + item.getAvatarUpdateTime() + ".jpg", "");
                if (dataFile1.exists()) {
                    Glide.with(mContext)
                            .load(dataFile1)
                            .apply(AppConfig.getInstance().optionsMainColor)
                            .into(ivLeft);
                } else {
                    loadAvatar(item);
                    Glide.with(mContext)
                            .load(R.mipmap.img_default_avatar)
                            .apply(AppConfig.getInstance().optionsMainColor)
                            .into(ivLeft);
                }
            } else {
                Glide.with(mContext)
                        .load(API.BASE_URL + item.getAvatar().replace("\\", "/"))
                        .apply(AppConfig.getInstance().optionsMainColor)
                        .into(ivLeft);
            }
        }
    }

    private void loadAvatar(Message message) {
//        if (message == null || message.getAvatarUpdateTime() == null || "0".equals(message.getAvatarUpdateTime())) {
//            return;
//        }
//        KLog.i("头像的名字为：" + message.getAvatarUpdateTime());
//        KLog.i("开始加载远程头像");
//        if (ConstantValue.loadingAvatarList.contains(message.getAvatarUpdateTime())) {
//            KLog.i("已经在加载，过滤掉");
//            return;
//        }
//        int friendNum = qlinkcom.GetFriendNumInFriendlist(message.getP2pId());
//        byte[] p2pId = new byte[100];
//        if (qlinkcom.GetFriendP2PPublicKey(message.getP2pId(), p2pId) == 0) {
//            KLog.i(new String(p2pId).trim());
//        }
//        if (friendNum >= 0) {
//            Qsdk.getInstance().addFileSendRequest(friendNum);
//        } else {
//            friendNum = qlinkcom.AddFriend(message.getP2pId());
//            if (friendNum >= 0) {
//                Qsdk.getInstance().addFileSendRequest(friendNum);
//            }
//        }
//        ConstantValue.loadingAvatarList.add(message.getAvatarUpdateTime());
    }
}
