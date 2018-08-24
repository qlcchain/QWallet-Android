package com.stratagile.qlink.utils;

import java.text.DecimalFormat;

public class QlcCountUtil {
    public static String parseQlc(float qlc) {
        if (qlc < 1000000) {
            if (qlc < 1000) {
                return qlc + "";
            } else {
                float count = qlc / 1000;
                DecimalFormat df = new DecimalFormat(".00");
                return df.format(count) + "K";
            }
        } else {
            float count = qlc / 1000000;
            DecimalFormat df = new DecimalFormat(".00");
            return df.format(count) + "M";
        }
    }

}
