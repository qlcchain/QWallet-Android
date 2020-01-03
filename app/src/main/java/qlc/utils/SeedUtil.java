package qlc.utils;

import java.security.SecureRandom;

public final class SeedUtil {
	
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private SeedUtil() {}

    public static byte[] generateSeed() {
        byte[] seed = new byte[32];
        SECURE_RANDOM.nextBytes(seed);
        return seed;
    }

    public static boolean isValid(byte[] seed) {
        return seed.length == 32;
    }
}
