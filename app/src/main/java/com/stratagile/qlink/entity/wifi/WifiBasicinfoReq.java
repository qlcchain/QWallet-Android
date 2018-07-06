package com.stratagile.qlink.entity.wifi;

/**
 * Created by huzhipeng on 2018/1/19.
 * {"data": "{ "ssid": "YYM-5", "mac":"00:0c:29:86:d9:94"}"}
 * 自定义的实体类，获取wifi基本信息的请求
 */


public class WifiBasicinfoReq {

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
