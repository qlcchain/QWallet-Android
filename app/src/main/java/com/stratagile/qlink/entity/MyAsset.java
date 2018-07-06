package com.stratagile.qlink.entity;

import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.WifiEntity;

/**
 * Created by huzhipeng on 2018/2/11.
 */

public class MyAsset {
    public static final int WIFI_ASSET = 0;
    public static final int VPN_ASSET = 1;
    public static final int VPN_ASSET_1 = 3;
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

    public WifiEntity getWifiEntity() {
        return wifiEntity;
    }

    public void setWifiEntity(WifiEntity wifiEntity) {
        this.wifiEntity = wifiEntity;
    }

    /**
     * 资产类型
     * 0，wifi
     * 1，vpn
     */

    private int type;
    private VpnEntity vpnEntity;
    private WifiEntity wifiEntity;
}
