package qlc.utils;

import com.rfksystems.blake2b.Blake2b;

public final class HashUtil {
	
    private static final int DIGEST_256 = 256 / 8;

    private HashUtil() {}

    public static byte[] digest256(byte[]... bytes) {
        return digest(DIGEST_256, bytes);
    }

    public static byte[] digest(int digestSize, byte[]... byteArrays) {
    	if (byteArrays == null) {
    		throw new NullPointerException("Byte Arrays can't be null");
    	}

        Blake2b blake2b = new Blake2b(null, digestSize, null, null);

        for (byte[] byteArray: byteArrays) {
        	blake2b.update(byteArray, 0, byteArray.length);
        }

        byte[] output = new byte[digestSize];
        blake2b.digest(output, 0);
        return output;
    }
}
