package com.stratagile.qlink.utils;

import android.content.Context;
import android.os.Build;
import android.os.LocaleList;

import com.socks.library.KLog;

import java.util.Locale;

public class LanguageUtil {
    public static boolean isCN(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = context.getResources().getConfiguration().getLocales();
            if (localeList.get(0).getLanguage().equals("zh")) {
                return true;
            } else {
                return false;
            }
        } else {
            locale = context.getResources().getConfiguration().locale;
            if (locale.getLanguage().equals("zh")) {
                return true;
            } else {
                return false;
            }
        }
    }
}
