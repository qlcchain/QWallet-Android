package com.stratagile.qlink.entity.vpn;

/**
 * Created by huzhipeng on 2018/2/9.
 */

public class VpnPrivateKeyReq {
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
