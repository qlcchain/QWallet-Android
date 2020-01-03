package qlc.utils;

import java.math.BigInteger;
import java.util.Arrays;

public class Helper {
	
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static byte[] wipe(byte[] b) {
        Arrays.fill(b, (byte) 0);
        return b;
    }
	
    public static byte[] reverse(byte[] value) {

        if (value==null || value.length==0) {
            return new byte[0];
        }
    	
        byte[] result = new byte[value.length];
        for (int i=0; i<value.length; i++) {
            result[i] = value[value.length-i-1];
        }
        return result;
    }
    
    public static byte[] hexStringToBytes(String value) {
        if (value==null || value.length()==0) {
            return new byte[0];
        }
        if (value.length()%2 == 1) {
            throw new IllegalArgumentException();
        }
        byte[] result = new byte[value.length()/2];
        for (int i=0; i<result.length; i++) {
            result[i] = (byte) Integer.parseInt(value.substring(i*2, i*2+2), 16);
        }
        return result;
    }
    
    public static String reverse(String value) {
    	return byteToHexString(reverse(hexStringToBytes(value)));
    }

	public static String hexStringToBinary(String hex) {
		String value = new BigInteger(hex, 16).toString(2);
		String formatPad = "%" + (hex.length()*4) + "s";
		return (String.format(formatPad, value).replace(" ", ""));
	}
	
	public static String binaryToHexString(String binary) {
		BigInteger num = new BigInteger(binary, 2);
		return num.toString(16).toUpperCase();
	}
	
    public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
    
    public static byte[] LongToBytes(long values) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((values >> offset) & 0xff);
        }
        return buffer;
    }
    
    public static byte[] bigInttoBytes(BigInteger value) {
        if (value == null) {
            return new byte[0];
        }
        byte[] tmp = value.toByteArray();
        byte[] res = new byte[tmp.length - 1];
        if (tmp[0] == 0) {
            System.arraycopy(tmp, 1, res, 0, res.length);
            return res;
        }

        return tmp;
    }

    public static byte[] toByteArray(BigInteger value) {
        if (value == null) {
            return new byte[0];
        }
        byte[] bytes = value.toByteArray();
        byte[] tmp = new byte[16];
        int sourcePosition = bytes.length <= 16 ? 0 : 1;
        int bytesLength = bytes.length <= 16 ? bytes.length : 16;
        System.arraycopy(bytes, sourcePosition, tmp, tmp.length - bytesLength, bytesLength);
        return tmp;
    }
    
    public static String byteToHexString(byte[] value) {

        if (value==null || value.length==0) {
            return null;
        }
        
        char[] hexChars = new char[value.length * 2];
        for (int j = 0; j < value.length; j++) {
            int v = value[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static String leftPad(String str, int size) {
        if (str.length() >= size) {
            return str;
        }

        StringBuilder builder = new StringBuilder();
        while (str.length() + builder.length() < size) {
            builder.append("0");
        }
        return builder.append(str).toString();
    }
}
