package com.stratagile.qlink.entity.wifi;

/**
 * Created by huzhipeng on 2018/1/23.
 * 获取好友当前使用的wifi的ssid和mac地址的请求
 */

public class WifiCurrentConnectReq {
    private String ssid;
    private String mac;

    public WifiCurrentConnectReq(String ssid, String macAddress) {
        this.ssid = ssid;
        this.mac = macAddress;
    }

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
