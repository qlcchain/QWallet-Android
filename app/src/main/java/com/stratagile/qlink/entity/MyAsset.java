package com.stratagile.qlink.entity;

import com.stratagile.qlink.db.VpnEntity;

/**
 * Created by huzhipeng on 2018/2/11.
 */

public class MyAsset {
    public static final int WIFI_ASSET = 0;
    public static final int VPN_ASSET_1 = 3;
    public static final int VPN_ASSET = 1;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public VpnEntity getVpnEntity() {
        return vpnEntity;
    }

    public void setVpnEntity(VpnEntity vpnEntity) {
        this.vpnEntity = vpnEntity;
    }


    /**
     * 资产类型
     * 0，wifi
     * 1，vpn
     */

    private int type;
    private VpnEntity vpnEntity;
}
