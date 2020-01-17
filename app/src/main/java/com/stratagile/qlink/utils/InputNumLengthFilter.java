package com.stratagile.qlink.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * ggband
 * 限制 小数和整数的位数
 */
public class InputNumLengthFilter implements InputFilter {

    private int maxPoint;
    private int maxInteger;
    public InputNumLengthFilter(int maxPoint, int maxInteger) {
        this.maxPoint = maxPoint;
        this.maxInteger = maxInteger;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int maxLength = maxInteger + maxPoint + 1;
        // 删除等特殊字符，直接返回
        if (nullFilter(source)) return null;
        String dValue = dest.toString();
        String[] splitArray = dValue.split("\\.");//在点前后分开两段
        if (splitArray.length > 0) {
            String intValue = splitArray[0];
            int errorIndex = dValue.indexOf(".");
            if (errorIndex == -1) {
                errorIndex = dValue.length();
            }
            if (intValue.length() >= maxLength - maxPoint - 1 && dstart <= errorIndex) {
                if (".".equals(source.toString())) {
                    return null;
                }
                return "";
            }
        }
        if (splitArray.length > 1 && dstart == dValue.length()) {
            String dotValue = splitArray[1];
            int diff = dotValue.length() + 1 - maxPoint;
            if (diff > 0) {
                try {
                    return source.subSequence(start, end - diff);
                } catch (IndexOutOfBoundsException e) {
                    return source;
                }
            }
        }
        if (dest.length() == maxLength - 1 && ".".equals(source.toString())) {
            return "";
        }
        if (dest.length() >= maxLength) {
            return "";
        }
        return null;
    }

    //  第一个参数是小数部分的位数，第二个参数是总长度（包括小数点）

    private boolean nullFilter(CharSequence source) {
        return source.toString().isEmpty();
    }
}
