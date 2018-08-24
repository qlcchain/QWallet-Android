package com.stratagile.qlink.entity.vpn;

/**
 * Created by huzhipeng on 2018/2/9.
 */

public class VpnPrivateKeyRsp {
    private String vpnName;
    private String privateKey;

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
