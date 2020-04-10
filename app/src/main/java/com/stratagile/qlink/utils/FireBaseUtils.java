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

    public static String Me_Referral_Rewards = "Me_Referral_Rewards";
    public static String Me_Join_the_community = "Me_Join_the_community";
    public static String Community_Twitter = "Community_Twitter";
    public static String Community_Telegram = "Community_Telegram";
    public static String Community_Facebook = "Community_Facebook";
    public static String Community_QLC_Chain = "Community_QLC_Chain";
    public static String Topup_China = "Topup_China";
    public static String Topup_Singapore = "Topup_Singapore";
    public static String Topup_Indonesia = "Topup_Indonesia";
    public static String Topup_Choose_a_plan = "Topup_Choose_a_plan";
    public static String Topup_Recharge_directly = "Topup_Recharge_directly";
    public static String Topup_Group_Plan = "Topup_Group_Plan";
    public static String Topup_order_confirm = "Topup_order_confirm";
    public static String Topup_Confirm_buy = "Topup_Confirm_buy";
    public static String Topup_Confirm_Cancel = "Topup_Confirm_Cancel";
    public static String Topup_Confirm_Send_QGas = "Topup_Confirm_Send_QGas";
    public static String Topup_Confirm_Send_QLC = "Topup_Confirm_Send_QLC";
    public static String Topup_GroupPlan_10_off = "Topup_GroupPlan_10%off";
    public static String Topup_GroupPlan_20_off = "Topup_GroupPlan_20%off";
    public static String Topup_GroupPlan_30_off = "Topup_GroupPlan_30%off";
    public static String Topup_GroupPlan_Stake = "Topup_GroupPlan_Stake";
    public static String Wallet_MyStakings_InvokeNewStakings = "Wallet_MyStakings_InvokeNewStakings";
    public static String Wallet_MyStakings_InvokeNewStakings_Invoke = "Wallet_MyStakings_InvokeNewStakings_Invoke";
    public static String Topup_Home_PartnerPlan_Be_Recharge_Partner = "Topup_Home_PartnerPlan_Be_Recharge_Partner";
    public static String Topup_Home_MyReferralCode_Share = "Topup_Home_MyReferralCode_Share";
    public static String Me_Settings_Languages = "Me_Settings_Languages";
    public static String Me_Settings_Languages_English = "Me_Settings_Languages_English";
    public static String Me_Settings_Languages_Chinese = "Me_Settings_Languages_Chinese";
    public static String Me_Settings_Languages_Indonesian = "Me_Settings_Languages_Indonesian";
    public static void logEvent(Context context, String event) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, event);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, event);
        FirebaseAnalytics.getInstance(context).logEvent(event, bundle);
    }
}
