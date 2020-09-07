package com.stratagile.qlink.utils;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;

import java.util.UUID;

public class UuidUtil {
    public static String getUUid2() {
        String uuid = SpUtil.getString(AppConfig.getInstance(), ConstantValue.uuid2, "");
        if ("".equals(uuid)) {
            uuid = FileUtil.readData("/Qwallet/uuid.txt");
            if ("".equals(uuid)) {
                uuid = UUID.randomUUID().toString().replace("-", "");
                FileUtil.savaData("/Qwallet/uuid.txt", uuid);
            }
        }
        KLog.i(uuid);
        return uuid;
    }
}
