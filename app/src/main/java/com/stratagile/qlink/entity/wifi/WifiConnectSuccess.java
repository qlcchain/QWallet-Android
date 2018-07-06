package com.stratagile.qlink.entity.wifi;

/**
 * Created by huzhipeng on 2018/1/24.
 * 告诉wifi提供者，使用者连接wifi成功了。
 */

public class WifiConnectSuccess {
    private int friendNum;
    private String ssid;

    public WifiConnectSuccess(int friendNum, String ssid) {
        this.friendNum = friendNum;
        this.ssid = ssid;
    }

    public int getFriendNum() {
        return friendNum;

    }

    public void setFriendNum(int friendNum) {
        this.friendNum = friendNum;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
