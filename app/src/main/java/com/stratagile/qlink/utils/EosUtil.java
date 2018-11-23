package com.stratagile.qlink.utils;

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
}
