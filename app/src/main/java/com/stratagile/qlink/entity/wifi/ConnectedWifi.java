package com.stratagile.qlink.entity.wifi;

/**
 * Created by huzhipeng on 2018/1/31.
 */

public class ConnectedWifi {
    private String friendNum;

    public ConnectedWifi(String friendNum, long updateTime, String ssid) {
        this.friendNum = friendNum;
        this.updateTime = updateTime;
        this.ssid = ssid;
    }

    private long updateTime;
    private String ssid;

    public String getFriendNum() {
        return friendNum;
    }

    public void setFriendNum(String friendNum) {
        this.friendNum = friendNum;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
