package com.stratagile.qlink.utils;

import android.content.ContentResolver;
import android.content.Context;


import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hjk on 2018/9/14.
 */

public class DateUtil {
    public static String getMaturityDate(int days, Context context) {
        long timeStamp = System.currentTimeMillis() + (24L * 60L * 60L *1000L * days);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(timeStamp);
        return getTimestampString(timeStamp, context);
    }


    public static String getTimestampString(Date var0, Context context) {
        String var1 = null;
        String var2 = Locale.getDefault().getLanguage();
        boolean var3 = var2.startsWith("zh");
        long var4 = var0.getTime();
        if (isSameYear(var0.getTime())) {
            var1 = "dd MMM";
        } else {
            var1 = "dd MMM YYYY";
        }
        if (Locale.getDefault().getLanguage().startsWith("zh")) {
            return (new SimpleDateFormat(var1, Locale.CHINA)).format(var0);
        } else {
            return (new SimpleDateFormat(var1, Locale.ENGLISH)).format(var0);
        }
    }

    public static String getTimestampString(long var0, Context context) {
        long result = var0 / 1000000000;
        Date date;
        if (result > 10) {
            date = new Date(var0);
        } else {
            date = new Date(var0 * 1000);
        }
        if (Locale.getDefault().getLanguage().startsWith("zh")) {
            String var1;
            if (isSameYear(var0)) {
                var1 = "MMM dd";
                KLog.i("同一年");
            } else {
                var1 = "yyyy MMM dd";
                KLog.i("不同年");
            }
            return (new SimpleDateFormat(var1, Locale.CHINA)).format(date);
        } else {
            String var1;
            if (isSameYear(var0)) {
                var1 = "dd MMM";
                KLog.i("同一年");
            } else {
                var1 = "dd MMM yyyy";
                KLog.i("不同年");
            }
            return (new SimpleDateFormat(var1, Locale.ENGLISH)).format(date);
        }
    }

    private static boolean isSameYear(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int otherYear = calendar.get(Calendar.YEAR);
        return thisYear == otherYear;
    }
}
