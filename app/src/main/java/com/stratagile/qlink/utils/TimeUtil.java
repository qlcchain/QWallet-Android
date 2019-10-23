package com.stratagile.qlink.utils;

import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.view.View;

import com.socks.library.KLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by hu on 2017/5/5.
 */

public class TimeUtil {

    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14 16:09:00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static long timeStamp(String time) {
        time = timeConvert(time);
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date;
//        KLog.i(time);
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
//            KLog.i(l);
            return l;
        } catch (Exception e) {
            e.printStackTrace();
            return new Long("123");
        }
    }

    /**
     * @return
     */
    public static String getTime() {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sdr.format(new Date(Calendar.getInstance().getTimeInMillis()));
    }

    public static String getTransactionTime(long timestamp) {
        timestamp = timestamp * 1000;
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
        return sdr.format(new Date(timestamp));
    }

    public static String getRevokeTime(long timestamp) {
        timestamp = timestamp * 1000;
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd ", Locale.CHINA);
        return sdr.format(new Date(timestamp));
    }

    public static String getOrderTime(long timestamp) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdr.format(new Date(timestamp));
    }

    public static String getStakeTime(long timestamp) {
        timestamp = timestamp * 1000;
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdr.format(new Date(timestamp));
    }

    public static String getTransactionHistoryTime(long timestamp) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdr.format(new Date(timestamp));
    }

    /**
     * 时区 时间转换方法:将传入的时间（可能为其他时区）转化成目标时区对应的时间
     *
     * @param sourceTime 时间格式必须为：yyyy-MM-dd HH:mm:ss
//     * @param sourceId   入参的时间的时区id 比如：+08:00
//     * @param targetId   要转换成目标时区id 比如：+09:00
//     * @param reFormat   返回格式 默认：yyyy-MM-dd HH:mm:ss
     * @return string 转化时区后的时间
     */
    public static String timeConvert(String sourceTime) {
        //校验入参是否合法
        String sourceId = "GMT+08:00";
        String targetId = getTimeZone();
        if (null == sourceId || "".equals(sourceId) || null == targetId
                || "".equals(targetId) || null == sourceTime
                || "".equals(sourceTime)) {
            return null;
        }

        String reFormat = "yyyy-MM-dd HH:mm:ss";

        //校验 时间格式必须为：yyyy-MM-dd HH:mm:ss
        String reg = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$";
        if (!sourceTime.matches(reg)) {
            return null;
        }

        try {
            //时间格式
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //根据入参原时区id，获取对应的timezone对象
            TimeZone sourceTimeZone = TimeZone.getTimeZone(sourceId);
            //设置SimpleDateFormat时区为原时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成原时区对应的date对象
            df.setTimeZone(sourceTimeZone);
            //将字符串sourceTime转化成原时区对应的date对象
            java.util.Date sourceDate = df.parse(sourceTime);

            //开始转化时区：根据目标时区id设置目标TimeZone
            TimeZone targetTimeZone = TimeZone.getTimeZone(targetId);
            //设置SimpleDateFormat时区为目标时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成目标时区对应的date对象
            df.setTimeZone(targetTimeZone);
            //得到目标时间字符串
            String targetTime = df.format(sourceDate);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = sdf.parse(targetTime);
            sdf = new SimpleDateFormat(reFormat);

            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTimeZone(){
        Calendar mDummyDate;
        mDummyDate = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        mDummyDate.setTimeZone(now.getTimeZone());
        mDummyDate.set(now.get(Calendar.YEAR), 11, 31, 13, 0, 0);
        return getTimeZoneText(now.getTimeZone(),true);
    }

    public static String getTimeZoneText(TimeZone tz, boolean includeName) {
        Date now = new Date();

        SimpleDateFormat gmtFormatter = new SimpleDateFormat("ZZZZ");
        gmtFormatter.setTimeZone(tz);
        String gmtString = gmtFormatter.format(now);
        BidiFormatter bidiFormatter = BidiFormatter.getInstance();
        Locale l = Locale.getDefault();
        boolean isRtl = TextUtils.getLayoutDirectionFromLocale(l) == View.LAYOUT_DIRECTION_RTL;
        gmtString = bidiFormatter.unicodeWrap(gmtString,
                isRtl ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR);

        if (!includeName) {
            return gmtString;
        }

        return gmtString;
    }
}
