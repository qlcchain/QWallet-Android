package com.stratagile.qlink.entity.wifi;

/**
 * Created by huzhipeng on 2018/1/19.
 * 连接wifi时，获取wifi密码的返回，
 */

public class WifipasswordRsp {
    /**
     * ssid : YYM-5
     * mac : 00:0c:29:86:d9:94
     */

    private String ssid;
    private String mac;

    private String friendNum;

    public String getFriendNum() {
        return friendNum;
    }

    public void setFriendNum(String friendNum) {
        this.friendNum = friendNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
