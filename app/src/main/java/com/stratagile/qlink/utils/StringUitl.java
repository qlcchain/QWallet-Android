package com.stratagile.qlink.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.push.service.aq;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author wwx
 * @ClassName: StringUitl
 * @Description: 字符串工具类
 * @date 2015年7月22日 下午3:03:36
 */
public class StringUitl {
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 正则：身份证号码15位
     */
    public static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * 正则：身份证号码18位
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";

    /**
     * @param str
     * @return boolean
     * @throws
     * @Title: isEmepty
     * @Description: 判断字符串是否为空和空串
     */
    public static boolean isNoEmpty(String str) {
        if (str == null) {
            return false;
        } else if ("".equals(str)) {
            return false;
        }
        return true;
    }

    /**
     * @param edittext
     * @param isEmptyStr 为空串提示语
     * @return boolean
     * @throws
     * @Title: hasEmptyItem
     * @Description: 判断输入框EditText是否不为空和空串
     */
    public static boolean isNotEmpty(EditText edittext, String isEmptyStr) {
        if (edittext.getText() == null) {
            if (isEmptyStr != null) {
            }
            return false;
        } else if ("".equals(edittext.getText().toString())) {
            if (isEmptyStr != null) {
            }
            return false;
        }
        return true;
    }
    /**
     * @param textView
     * @param isEmptyStr 为空串提示语
     * @return boolean
     * @throws
     * @Title: hasEmptyItem
     * @Description: 判断输入框EditText是否不为空和空串
     */
    public static boolean isNotEmpty(TextView textView, String isEmptyStr) {
        if (textView.getText() == null) {
            if (isEmptyStr != null) {
            }
            return false;
        } else if ("".equals(textView.getText().toString())) {
            if (isEmptyStr != null) {
            }
            return false;
        }
        return true;
    }

    /**
     * @param context
     * @param text
     * @param isEmptyStr 为空串提示语
     * @return boolean
     * @throws
     * @Title: hasEmptyItem
     * @Description: 判断输入框EditText是否不为空和空串
     */
    public static boolean isNotEmpty(Context context, String text,
                                     String isEmptyStr) {
        if (text == null) {
            if (isEmptyStr != null) {
                Toast.makeText(context, isEmptyStr, Toast.LENGTH_LONG).show();
            }
            return false;
        } else if ("".equals(text.toString())) {
            if (isEmptyStr != null) {
                Toast.makeText(context, isEmptyStr, Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    /**
     * @param i
     * @return String
     * @throws
     * @Title: intToIp
     * @Description: 转换为Ip地址
     */
    public static String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    /**
     * 验证字符串是否是手机号码
     */
    public static boolean isMobileNum(String num) {
        String expression = "^((13[0-9])|(17[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,0-9]))\\d{8}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(num);
        return matcher.matches();
    }

    /**
     * @param list
     * @return List
     * @throws
     * @Title: removeDuplicate
     * @Description: 移除list重复元素
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    // 获取当月的 天数
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static int getCurYear() {
        Calendar a = Calendar.getInstance();
        return a.get(Calendar.YEAR);
    }

    public boolean isNull(String str) {
        return (str == null) || (str.trim().length() == 0);
    }

    public boolean isIdCard(String num) {
        if (isNull(num)) {
//			ToastUtil.show(this,"身份证不能为空");
            return false;
        }

        if (num.length() == 18 || num.length() == 15) {
            return true;
        }
//		ToastUtil.show(this,"身份证长度不正确");
        return false;
    }

    /**
     * 验证身份证号码15位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard15(CharSequence input) {
        return isMatch(REGEX_ID_CARD15, input);
    }

    /**
     * 验证身份证号码18位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard18(CharSequence input) {
        return isMatch(REGEX_ID_CARD18, input);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }


    public static String Md5(String plainText) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString(); //md5 32bit
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * SHA256加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */
    public static String encryptSHA256ToString(String data) {
        return encryptSHA256ToString(data.getBytes());
    }


    /**
     * SHA256加密
     *
     * @param data 明文字节数组
     * @return 16进制密文
     */
    public static String encryptSHA256ToString(byte[] data) {
        return bytes2HexString(encryptSHA256(data));
    }

    /**
     * SHA256加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public static byte[] encryptSHA256(byte[] data) {
        return hashTemplate(data, "SHA256");
    }

    /**
     * hash加密模板
     *
     * @param data      数据
     * @param algorithm 加密算法
     * @return 密文字节数组
     */
    private static byte[] hashTemplate(byte[] data, String algorithm) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * byteArr转hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节数组
     * @return 16进制大写字符串
     */
    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }


//	/**
//	 * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
//	 *
//	 * 渠道标志为：
//	 * 1，andriod（a）
//	 *
//	 * 识别符来源标志：
//	 * 1， wifi mac地址（wifi）；
//	 * 2， IMEI（imei）；
//	 * 3， 序列号（sn）；
//	 * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
//	 * @param context
//	 * @return
//	 */
//	public static String getDeviceId(Context context) {
//
//		StringBuilder deviceId = new StringBuilder();
//// 渠道标志
//		deviceId.append("a");
//		try {
////wifi mac地址
//			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//			WifiInfo info = wifi.getConnectionInfo();
//			String wifiMac = info.getMacAddress();
//			if (!hasEmptyItem(wifiMac)) {
//				deviceId.append("wifi");
//				deviceId.append(wifiMac);
//				Log.e("geek : ", "wifi mac=" + deviceId.toString());
//				return deviceId.toString().replace(":","");
//			}
////IMEI（imei）
//			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//			String imei = tm.getDeviceId();
//			if (!hasEmptyItem(imei)) {
//				deviceId.append("imei");
//				deviceId.append(imei);
//				Log.e("geek : ", "IMEI（imei）=" + deviceId.toString());
//				return deviceId.toString();
//			}
//
////序列号（sn）
//			String sn = tm.getSimSerialNumber();
//			if (!hasEmptyItem(sn)) {
//				deviceId.append("sn");
//				deviceId.append(sn);
//				Log.e("geek : ", "序列号（sn）=" + deviceId.toString());
//				return deviceId.toString();
//			}
//		} catch (Exception e) {
//			Log.d("geek", "getDeviceId: e");
//		}
//		return "";
//	}

    /**
     * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     * <p>
     * 渠道标志为：
     * 1，andriod（a）
     * <p>
     * 识别符来源标志：
     * 1， wifi mac地址（wifi）；
     * 2， IMEI（imei）；
     * 3， 序列号（sn）；
     * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");
        try {
//            //wifi mac地址
//            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo info = wifi.getConnectionInfo();
//            String wifiMac = info.getMacAddress();
//            if (!hasEmptyItem(wifiMac)) {
//                deviceId.append("wifi");
//                deviceId.append(wifiMac);
//            }
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!hasEmptyItem(imei)) {
                deviceId.append("m");
                deviceId.append(imei);
            }

            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!hasEmptyItem(sn)) {
                deviceId.append("s");
                deviceId.append(sn);
                Log.e("geek : ", "序列号（sn）=" + deviceId.toString());
            }

            return deviceId.toString();
        } catch (Exception e) {
            Log.d("geek", "getDeviceId: e");
//			deviceId.append("e"+deviceId.toString()+ JPushInterface.getRegistrationID(context));
        }
        return deviceId.toString();
    }
////如果上面都没有， 则生成一个id：随机码
//			String uuid = getUUID(context);
//			if(!hasEmptyItem(uuid)){
//				deviceId.append("id");
//				deviceId.append(uuid);
//				PALog.e("getDeviceId : ", deviceId.toString());
//				return deviceId.toString();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			deviceId.append("id").append(getUUID(context));
//		}
//
//		Log.e("getDeviceId : ", deviceId.toString());
//
//		return deviceId.toString();

    /**
     * 得到全局唯一UUID
     */
//	public static String getUUID(Context context){
//		String uuid = "";
//		SharedPreferences mShare = getSysShare(context, "sysCacheMap");
//		if(mShare != null){
//			uuid = mShare.getString("uuid", "");
//		}
//
//		if(hasEmptyItem(uuid)){
//			uuid = UUID.randomUUID().toString();
//			saveSysMap(context, "sysCacheMap", "uuid", uuid);
//		}
//
//		Log.e("getDeviceId : ", "getUUID : " + uuid);
//		return uuid;
//	}
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 获取手机品牌
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取Android系统版本
     */
    public static String getPhoneSysVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 禁止edittext输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {

        InputFilter filter = new InputFilter() {
            Pattern pattern = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】  <>《》]|[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher matcher = pattern.matcher(source);
                if (matcher.find()) return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 只限输入中文和英文，50个字
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeOnlyChnese50(EditText editText) {
        InputFilter filter = new InputFilter() {
            Pattern pattern = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】  <>《》]|[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher matcher = pattern.matcher(source);
                if (matcher.find()) return "";
                else return null;
            }
        };
        InputFilter lengthFilter = new InputFilter.LengthFilter(50);
        editText.setFilters(new InputFilter[]{filter, lengthFilter});
    }

    /**
     * 只限输入中文和英文 10个字
     * @param editText
     */
    public static void setInputName(EditText editText) {
        InputFilter filter = new InputFilter() {
            Pattern pattern = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】  <>《》]|[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher matcher = pattern.matcher(source);
                if (matcher.find()) return "";
                else return null;
            }
        };
        InputFilter lengthFilter = new InputFilter.LengthFilter(10);
        editText.setFilters(new InputFilter[]{filter, lengthFilter});
    }

    //[\\u4e00-\\u9fa5]+   中文过滤器
    //[a-zA-Z /]+    英文过滤器
    //[0-9]*         数字过滤器
    // [`~!@#$%^&*()+=|{}':;',\[\].<>/?~！@#￥%……&*（）——+|{}【】  <>《》]|[🀀-🏿]|[🐀-🟿]|[☀-⟿]   特殊字过滤
    //[，。？！；、‘“’”：]   中文标点

    /**
     * 测试用的
     * @param editText
     */
    public static void setMyInput(EditText editText) {
        InputFilter filter = new InputFilter() {
            Pattern pattern = Pattern.compile("![\\u4e00-\\u9fa5]+&![a-zA-Z /]+ &![0-9]*&![，。？！；、‘“’”：]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher matcher = pattern.matcher(source);
                if (matcher.find()) return "";
                else return null;
            }
        };
        InputFilter lengthFilter = new InputFilter.LengthFilter(10);
        editText.setFilters(new InputFilter[]{filter, lengthFilter});
    }

    public static String replaceEndFenHao(String string) {
        if (string.contains(";")) {
            String[] split = string.split(";");
            return split[0];
        }
        return string;
    }

    /**
     * 得到现在时间
     *
     * @return 字符串 yyyyMMdd
     */
    public static String getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取时间 小时 分 秒
     *
     * @return 字符串 HHmmss
     */
    public static String getTimeShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static boolean hasEmptyItem(String... strings) {
        if (strings == null || strings.length == 0) {
            return true;
        }
        for (String str : strings) {
            if (str == null || "".equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String... strings) {
        if (strings == null || strings.length == 0) {
            return true;
        }
        for (String str : strings) {
            if (str != null && !"".equals(str)) {
                return false;
            }
        }
        return true;
    }




    public static boolean isTelNum(String num) {
        if (hasEmptyItem(num)) {
            return false;
        }
        String phone = "^((13[0-9])|(17[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,0-9]))\\d{8}$";
        String phone2 = "\\+\\d{2}((13[0-9])|(17[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,0-9]))\\d{8}";
        String tel = "0\\d+-*\\d+";
        if (num.matches(phone) ||
                num.matches(tel)
                || num.matches(phone2)) {
            return true;
        }

        return false;
    }

    /**
     * 模糊匹配
     * @param one 要比较的字段
     * @param anotherString 目标字段
     * @return
     */
    public static boolean isSimilar(String one, String anotherString) {
        int length = one.length();
        if(length > anotherString.length()) {
            //如果被期待为开头的字符串的长度大于anotherString的长度
            return false;
        }
        if (one.equalsIgnoreCase(anotherString.substring(0, length))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否都大写
     * @param souce
     * @return
     */
    public static boolean isAllSupperCase(String souce)
    {
        if(null == souce)
        {
            return false;
        }
        String trim = souce.replaceAll("\\d", "").trim();
        for (int i = 0 ; i < trim.length() ;i++)
        {
            if(!Character.isUpperCase(trim.charAt(i)))
            {
                return  false;
            }
        }
        return true;
    }
    public static boolean isBase64(String str) {

        if(str == null || "".equals(str))
        {
            return false;
        }
        //str = str.replace("\n","").replace(" ", "");
        return str.indexOf("+") >= 0 || str.indexOf("/") >= 0 || str.indexOf("=") >= 0;
       /* byte[] bytes  = Base64.decode(str, Base64.NO_WRAP);
        String temp = new String(bytes);
        return str.equals(Base64.encodeToString(bytes,Base64.NO_WRAP));*/
    }
    /**
     * 判断是否为乱码
     *
     * @param str
     * @return
     */
    public static boolean isMessyCode(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
            //从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
            //System.out.println("--- " + (int) c);
            if ((int) c == 0xfffd) {
                // 存在乱码
                //System.out.println("存在乱码 " + (int) c);
                return true;
            }
        }
        return false;
    }
}
