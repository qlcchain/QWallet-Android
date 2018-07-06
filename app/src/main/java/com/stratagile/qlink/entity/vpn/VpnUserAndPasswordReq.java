package com.stratagile.qlink.entity.vpn;

/**
 * Created by huzhipeng on 2018/2/9.
 * 连接vpn时，拿配置文件的用户名和密码的请求
 */

public class VpnUserAndPasswordReq {
    private String vpnName;

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }
}
