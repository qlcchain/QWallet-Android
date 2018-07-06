package com.stratagile.qlink.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by huzhipeng on 2018/1/10.
 */

public class SignUtil {

    private static String getSignature(String text, String key, String charset) {
        text = text + key;
        byte[] bytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(text.getBytes(charset));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString().toLowerCase(Locale.ENGLISH);
    }

    /**
     * @param params
     * @param key
     * @param charset
     * @return
     * @throws Exception
     */
    public static String getSignature(JSONObject params, String key, String charset) {
        return getSignature(getSortQueryString(params, charset, false, false), key, charset);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("addressFrom", "ALE8RGdjCzFBGewA6eUmEch6wm6ui5o2NK");  //连接人的钱包address
        map.put("addressTo", "AVgKb1TzCdKQLPwRDX9M4mZyJG1HwSBMqC");    //提供WiFi的人的钱包地址
        map.put("fromP2pId", "4462CD037C2FB4ADFC7C154EF103442DB313D9509CCA16A38594730D068F6F105557476F70EB");
        map.put("qlc", "7.5");
        map.put("recordId", "29b06701fcd949829ea1e5f1bca8fd89");
        map.put("toP2pId", "B773C104F30BE7875A85D08DAE0F39E1B81448776D3B38BD96BEB877D1484B078238BCC9508F");
        System.out.println(getSignature((JSONObject) JSONObject.toJSON(map), "05cd19c64d5f4faabd27c74607fd1f51", "UTF-8"));
    }

    /**
     * @param params
     * @param key
     * @param charset
     * @param allow
     * @return
     * @throws Exception
     */
    public static String getSignature(JSONObject params, String key, String charset, boolean allow) {
        return getSignature(getSortQueryString(params, charset, allow, false), key, charset);
    }

    /**
     * @param params
     * @param allow
     * @param encode
     * @return
     * @throws Exception
     */
    public static String getSortQueryString(JSONObject params, String charset, boolean allow, boolean encode) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // 先将参数以其参数名的字典序升序进行排序
            Map<String, Object> sortedParams = new TreeMap<String, Object>(params);
            Set<Map.Entry<String, Object>> entrys = sortedParams.entrySet();
            // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
            for (Map.Entry<String, Object> param : entrys) {
                if (!allow && isBlank(param.getValue().toString())) {
                    continue;
                }
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("&");
                }
//                KLog.i(param.getKey());
                //stringBuilder.append(param.getKey()).append("=").append(param.getValue());

                stringBuilder.append((param.getKey() != null ? (encode ? URLEncoder.encode(param.getKey(), charset) : param.getKey()) : ""));
                stringBuilder.append("=");
                stringBuilder.append(param.getValue() != null ? (encode ? URLEncoder.encode(param.getValue().toString(), charset) : param.getValue().toString()) : "");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        KLog.i("排序的结果为:" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * @param params
     * @return
     * @throws Exception
     */
    public static String getSortQueryString(JSONObject params) {
        return getSortQueryString(params, "UTF-8", true, false);
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
