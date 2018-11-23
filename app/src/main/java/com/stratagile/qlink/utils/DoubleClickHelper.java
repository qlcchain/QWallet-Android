package com.stratagile.qlink.utils;

import java.util.Calendar;

public class DoubleClickHelper {
    public static long lastTime = 0;
    public static boolean isDoubleClick() {
        if (Calendar.getInstance().getTimeInMillis() - lastTime < 2000) {
            return true;
        } else {
            lastTime = Calendar.getInstance().getTimeInMillis();
            return false;
        }
    }
}
