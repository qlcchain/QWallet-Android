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
    public static String Topup_Home_getMoreQGAS_ReferFriends = "Topup_Home_getMoreQGAS_ReferFriends";
    public static String Topup_Home_getMoreQGAS_StakeQLC = "Topup_Home_getMoreQGAS_StakeQLC";
    public static String Topup_Home_TradeMining_MoreDetails = "Topup_Home_TradeMining_MoreDetails";
    public static String Topup_Home_PartnerPlan_MoreDetails = "Topup_Home_PartnerPlan_MoreDetails";
    public static String Topup_Home_QGASBuyBack_MoreDetails = "Topup_Home_QGASBuyBack_MoreDetails";
    public static String Topup_Home_QGASBuyBack_JoinNow = "Topup_Home_QGASBuyBack_JoinNow";
    public static String Topup_Home_MyReferralCode_Copy = "Topup_Home_MyReferralCode_Copy";
    public static String Topup_Home_MyReferralCode_ReferNOW = "Topup_Home_MyReferralCode_ReferNOW";
    public static String Topup_Home_MyOrders = "Topup_Home_MyOrders";
    public static String Topup_Home_ChooseToken = "Topup_Home_ChooseToken";
    public static String Topup_MyOrders_GroupOrders = "Topup_MyOrders_GroupOrders";
    public static String Topup_MyOrders_BlockchainInvoice = "Topup_MyOrders_BlockchainInvoice";
    public static String Topup_MyOrders_Cancel = "Topup_MyOrders_Cancel";
    public static String Topup_MyOrders_PayNow = "Topup_MyOrders_PayNow";
    public static String OTC_Home_BUY = "OTC_Home_BUY";
    public static String OTC_Home_SELL = "OTC_Home_SELL";
    public static String OTC_Home_Record = "OTC_Home_Record";
    public static String OTC_Home_NewOrder = "OTC_Home_NewOrder";
    public static String OTC_Home_Filter = "OTC_Home_Filter";
    public static String OTC_NewOrder_BUY_Confirm = "OTC_NewOrder_BUY_Confirm";
    public static String OTC_NewOrder_SELL_Confirm = "OTC_NewOrder_SELL_Confirm";
    public static String OTC_SELL_Submit = "OTC_SELL_Submit";
    public static String OTC_BUY_Submit = "OTC_BUY_Submit";
    public static void logEvent(Context context, String event) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, event);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, event);
        FirebaseAnalytics.getInstance(context).logEvent(event, bundle);
    }
}
