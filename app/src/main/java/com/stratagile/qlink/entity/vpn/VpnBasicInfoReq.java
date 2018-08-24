package com.stratagile.qlink.entity.vpn;

/**
 * Created by huzhipeng on 2018/2/8.
 */

public class VpnBasicInfoReq {
    private String vpnName;
    private String p2pId;

    public String getP2pId() {
        return p2pId;
    }

    public void setP2pId(String p2pId) {
        this.p2pId = p2pId;
    }

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }
}
