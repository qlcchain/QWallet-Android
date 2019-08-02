package com.stratagile.qlink.utils;


import com.socks.library.KLog;
import com.stratagile.qlink.constant.ConstantValue;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountUtil {
    public static boolean isEmail(String str) {
        String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        return match(regex, str);
    }

    public static boolean isTelephone(String str) {
        String regex = "^(13|14|15|16|18)\\d{9}$";
        return match(regex, str);
    }

    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String getUserToken() {
        String orgin = Calendar.getInstance().getTimeInMillis() + "," + ConstantValue.currentUser.getPassword();
        String token = RSAEncrypt.encrypt(orgin, ConstantValue.currentUser.getPubKey());
        return token;
    }

    public static String setUserNickName(String nickName) {
        if (isEmail(nickName)) {
            return nickName.substring(0, 3) + "...";
        } else if (isTelephone(nickName)) {
            return nickName.substring(0, 3) + "****" + nickName.substring(7, 11);
        } else {
            return nickName;
        }
    }
}
