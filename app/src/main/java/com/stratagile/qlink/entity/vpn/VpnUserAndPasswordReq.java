package com.stratagile.qlink.entity.vpn;

/**
 * Created by huzhipeng on 2018/2/9.
 * 连接vpn时，拿配置文件的用户名和密码的请求
 */

public class VpnUserAndPasswordReq {

    public static String mainNet = "1";

    public static String testNet = "0";

    private String vpnName;

    private String isMainNet;

    public String getIsMainNet() {
        return isMainNet;
    }

    public void setIsMainNet(String isMainNet) {
        this.isMainNet = isMainNet;
    }

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }
}
