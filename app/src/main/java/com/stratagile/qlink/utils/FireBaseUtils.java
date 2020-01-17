package com.stratagile.qlink.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FireBaseUtils {
    public static String eventLogin = "login";
    public static String eventStartApp = "startApp";
    public static String eventRegiester = "regiester";
    public static String eventTradesMining = "tradesMining";
    public static String eventClaimQLC = "claimQLC";
    public static String eventTradeNow = "tradeNOW";
    public static void logEvent(Context context, String event) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, event);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, event);
        FirebaseAnalytics.getInstance(context).logEvent(event, bundle);
    }
}
