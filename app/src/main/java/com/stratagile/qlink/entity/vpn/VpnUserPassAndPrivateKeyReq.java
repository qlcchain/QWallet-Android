package com.stratagile.qlink.entity.vpn;

public class VpnUserPassAndPrivateKeyReq {
    private String vpnName;
    private String isMainNet;

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getIsMainNet() {
        return isMainNet;
    }

    public void setIsMainNet(String isMainNet) {
        this.isMainNet = isMainNet;
    }
}
