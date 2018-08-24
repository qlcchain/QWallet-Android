package com.stratagile.qlink.entity.vpn;

/**
 * Created by zl on 2018/08/10.
 * vpn server 配置
 */

public class WindowConfig {
    private String vpnName;
    private String vpnfileName;//手机存的配置文件路径
    private String serverVpnfileName;//服务器的配置文件路径
    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getVpnfileName() {
        return vpnfileName;
    }

    public void setVpnfileName(String vpnfileName) {
        this.vpnfileName = vpnfileName;
    }

    public String getServerVpnfileName() {
        return serverVpnfileName;
    }

    public void setServerVpnfileName(String serverVpnfileName) {
        this.serverVpnfileName = serverVpnfileName;
    }

}
