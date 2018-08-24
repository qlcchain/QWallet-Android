package com.stratagile.qlink.entity.qlink;

/**
 * Created by huzhipeng on 2018/1/30.
 * 请求vpn注册者发送vpn的配置文件
 */

public class SendFileRequest {
    private int friendNumber;

    public SendFileRequest(int friendNumber) {
        this.friendNumber = friendNumber;
    }

    public int getFriendNumber() {
        return friendNumber;
    }

    public void setFriendNumber(int friendNumber) {
        this.friendNumber = friendNumber;
    }
}
