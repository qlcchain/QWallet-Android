package com.stratagile.qlink.entity.im;

/**
 * Created by huzhipeng on 2018/3/20.
 * 邀请进入群组聊天的请求
 */

public class InviteToGroupChatReq {
    private String assetName;

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public int getAssetType() {
        return assetType;
    }

    public void setAssetType(int assetType) {
        this.assetType = assetType;
    }

    private int assetType;
}
