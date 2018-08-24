package com.stratagile.qlink.entity.wifi;

/**
 * Created by huzhipeng on 2018/1/19.
 * 连接wifi时，获取wifi密码的请求
 */

public class WifipasswordReq {
    /**
     * ssid : YYM-5
     * mac : 00:0c:29:86:d9:94
     */

    private String ssid;
    private String mac;

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
