package com.stratagile.qlink.utils;

/**
 * Created by huzhipeng on 2018/5/7.
 */

public class WalletKtutil {

    private static char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    public static String byteArrayToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            int octet = bytes[i];
            int firstIndex = (octet & 0xF0) >>> (4);
            int secondIndex = octet & 0x0F;
            result.append(HEX_CHARS[firstIndex]);
            result.append(HEX_CHARS[secondIndex]);
        }
        return result.toString();
    }

    public static byte[] hexStringToByteArray(String hex) {
        int length = hex.length();
        byte[] data = new byte[length / 2];
        int i = 0;
        while (i < length) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
            i += 2;
        }
        return data;
    }
}
