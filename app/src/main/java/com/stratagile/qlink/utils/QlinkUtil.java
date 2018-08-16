package com.stratagile.qlink.utils;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.qlinkcom;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huzhipeng on 2018/1/9.
 */

public class QlinkUtil {
    public static String getP2PId(Context context) {
        return "";
    }

    public static String parseMap2String(String type, Map map) {
        //加上统一的参数，app的版本信息
        map.put("appVersion", VersionUtil.getAppVersionCode(AppConfig.getInstance()));
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("type", type);
        returnMap.put("data", JSONObject.toJSON(map).toString());
        String result = JSONObject.toJSON(returnMap).toString();
        KLog.i("构造出来的结果为:" + result);
        LogUtil.addLog(result, "发送的消息为：");
        return result;
    }
    public static int parseMap2StringAndSend(String friendNum, String type, Map map) {
        KLog.i("parseMap2StringAndSend接收方的friendNum为：" + friendNum+"——my:"+qlinkcom.GetP2PConnectionStatus()+"——you:"+qlinkcom.GetFriendConnectionStatus(friendNum+"") +"_data"+ parseMap2String(type, map));
        int sendReuestResult = qlinkcom.SendRequest(friendNum, parseMap2String(type, map));
        KLog.i("parseMap2StringAndSend发送的结果为:" + sendReuestResult);
        return sendReuestResult;
    }
}
