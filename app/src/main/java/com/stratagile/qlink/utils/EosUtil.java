package com.stratagile.qlink.utils;

import com.socks.library.KLog;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EosUtil {
    /**
     * eos账号名
     *
     * @param eosName the eos name
     * @return boolean
     */
    public static boolean isEosName(String eosName) {
        String strPattern = "^[a-z]{1}[1-5a-z]{11}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(eosName);
        return m.matches();
    }

    public static String setEosValue(String value) {
        if (value == null || "".equals(value)) {
            value = "0.0";
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(value));
        KLog.i(bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS");
        return bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS";
    }
}
