package com.stratagile.qlink.utils;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.vpn.VpnUserAndPasswordReq;

public class VpnUtil {
    /**
     * 判断该资产和当前设备是否处于同一个网络
     * 同一个网络指，都为测试网或者都为正式网
     * @param vpnEntity
     * @return
     */
    public static boolean isInSameNet(VpnEntity vpnEntity) {
//        KLog.i("当前网络为：" + SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
//        KLog.i("资产: " + vpnEntity.getVpnName() + "网络为：" + vpnEntity.getIsMainNet());
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false) && vpnEntity.getIsMainNet()) {
            return true;
        }
        if (!SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false) && !vpnEntity.getIsMainNet()) {
            return true;
        }
        return false;
    }

    /**
     * 判断远程需要客户所处的网络和该资产所处的网络是否处于同一个网络
     * 同一个网络指，都为测试网或者都为正式网
     * @param remoteNet
     * @param selfVpnEntity
     * @return
     */
    public static boolean isInSameNet(String remoteNet, VpnEntity selfVpnEntity) {
        if (VpnUserAndPasswordReq.mainNet.equals(remoteNet) && selfVpnEntity.getIsMainNet()) {
            return true;
        }

        if (!VpnUserAndPasswordReq.mainNet.equals(remoteNet) && !selfVpnEntity.getIsMainNet()){
            return true;
        }

        return false;
    }
}
