package com.stratagile.qlink.utils;

import java.sql.Time;

/**
 * Created by huzhipeng on 2018/2/23.
 */

public class LogUtil {
    public static String mLogInfo = "";
    public static boolean isShowLog = true;
    public static void addLog(String logInfo, String classInfo) {
        if (isShowLog) {
            mLogInfo += classInfo + "  " + TimeUtil.getTime() + "  " + logInfo + "\n\n";
        }
    }

}
