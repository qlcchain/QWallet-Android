package com.stratagile.qlink.utils;

import com.socks.library.KLog;
import com.stratagile.qlink.db.UserAccount;

import java.util.Calendar;

public class UserUtils {
    public static String getUserToken(UserAccount userAccount) {
        String orgin = Calendar.getInstance().getTimeInMillis() + "," + userAccount.getPassword();
        String token = RSAEncrypt.encrypt(orgin, userAccount.getPubKey());
        return token;
    }
}
