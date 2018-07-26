package com.stratagile.qlink.utils;

import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.VpnEntity;

public class VpnUtil {
    public static boolean isInSameNet(VpnEntity vpnEntity) {
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false) && vpnEntity.getIsMainNet()) {
            return true;
        }
        if (!SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false) && !vpnEntity.getIsMainNet()) {
            return true;
        }
        return false;
    }
}
