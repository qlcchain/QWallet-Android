package com.stratagile.qlink.utils;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.socks.library.KLog;

import java.util.List;

/**
 * Created by huzhipeng on 2018/3/2.
 */

public class NetUtil {
    /**
     * 忘记某一个wifi密码
     *
     * @param wifiManage
     * @param targetSsid
     */
    public static void removeWifiBySsid(WifiManager wifiManage, String targetSsid) {
        KLog.i("try to removeWifiBySsid, targetSsid=" + targetSsid);
        List<WifiConfiguration> wifiConfigs = wifiManage.getConfiguredNetworks();
        for (WifiConfiguration wifiConfig : wifiConfigs) {
            String ssid = wifiConfig.SSID;
            KLog.i("removeWifiBySsid ssid=" + ssid);
            if (ssid.equals(targetSsid)) {
                KLog.i("removeWifiBySsid success, SSID = " + wifiConfig.SSID + " netId = " + String.valueOf(wifiConfig.networkId));
                wifiManage.removeNetwork(wifiConfig.networkId);
                wifiManage.saveConfiguration();
            }
        }
    }
}
