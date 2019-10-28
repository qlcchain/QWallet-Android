package com.stratagile.qlink.reciver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.PushExtra;
import com.stratagile.qlink.jiguang.TagAliasOperatorHelper;
import com.stratagile.qlink.ui.activity.main.TestActivity;
import com.stratagile.qlink.ui.activity.reward.MyClaimActivity;
import com.stratagile.qlink.utils.SystemUtil;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class JpushReceiver extends JPushMessageReceiver {
    private static final String TAG = "JpushReceiver";

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        KLog.i("[onMessage] " + customMessage);
        processCustomMessage(context, customMessage);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        KLog.i("[onNotifyMessageOpened] " + message);
        if (message.notificationExtras != null && !"".equals(message.notificationExtras)) {
            KLog.i(message.notificationExtras);
            PushExtra pushExtra = new Gson().fromJson(message.notificationExtras, PushExtra.class);
            try {
                if (SystemUtil.isAppaLive(context, "com.stratagile.qwallet")) {
                    switch (pushExtra.getSkip()) {
                        case "debit":
                            Intent i = new Intent(context, MyClaimActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, message.notificationTitle);
                            bundle.putString(JPushInterface.EXTRA_ALERT, message.notificationContent);
                            i.putExtras(bundle);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);
                            break;
                        case "":
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (pushExtra.getSkip()) {
                        case "debit":
                            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.stratagile.qwallet");
                            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            Bundle args = new Bundle();
                            args.putString("skip", "debit");
                            launchIntent.putExtra(ConstantValue.EXTRA_BUNDLE, args);
                            context.startActivity(launchIntent);
                            break;
                        case "":
                            break;
                        default:
                            break;
                    }
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        KLog.i("[onMultiActionClicked] 用户点击了通知栏按钮");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            KLog.i("ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            KLog.i("[onMultiActionClicked] 用户点击通知栏按钮一");
        } else if (nActionExtra.equals("my_extra2")) {
            KLog.i("[onMultiActionClicked] 用户点击通知栏按钮二");
        } else if (nActionExtra.equals("my_extra3")) {
            KLog.i("[onMultiActionClicked] 用户点击通知栏按钮三");
        } else {
            KLog.i("[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        KLog.i("[onNotifyMessageArrived] " + message);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        KLog.i("[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        KLog.i("[onRegister] " + registrationId);
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        KLog.i("[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        KLog.i("[onCommandResult] " + cmdMessage);
    }

//    @Override
//    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context,jPushMessage);
//        super.onTagOperatorResult(context, jPushMessage);
//    }
//    @Override
//    public void onCheckTagOperatorResult(Context context,JPushMessage jPushMessage){
//        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context,jPushMessage);
//        super.onCheckTagOperatorResult(context, jPushMessage);
//    }
//    @Override
//    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context,jPushMessage);
//        super.onAliasOperatorResult(context, jPushMessage);
//    }
//
//    @Override
//    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
//        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context,jPushMessage);
//        super.onMobileNumberOperatorResult(context, jPushMessage);
//    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, CustomMessage customMessage) {
//        if (MainActivity.isForeground) {
//            String message = customMessage.message;
//            String extras = customMessage.extra;
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//        }
    }

}
