package com.stratagile.qlink.constant;

/**
 * Created by huzhipeng on 2018/3/1.
 * 为什么有eventbus还要用broadcast，因为eventbus不能跨进程。
 */

public interface BroadCastAction {

    String disconnectVpnSuccess = "com.stratagile.qlink.disconnectVpnSuccess";
    String disconnectVpn = "com.stratagile.qlink.disconnectVpn";
    String disconnectWiFi = "com.stratagile.qlink.disconnectWiFi";
}
