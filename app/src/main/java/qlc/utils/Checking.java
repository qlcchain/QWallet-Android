package qlc.utils;

public final class Checking {
	
    private Checking() {}

    public static void check(boolean invalid, String msg) {
        if (invalid) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void checkHash(byte[] hash) {
    	check((hash==null || hash.length!=32), "Invalid hash length");
    }

    public static void checkSignature(byte[] signature) {
    	check((signature==null || signature.length!=64), "Invalid signature length");
    }

    public static void checkKey(byte[] key) {
    	check((key==null || key.length!=32), "Invalid key length");
    }

    public static void checkSeed(byte[] seed) {
    	check((seed==null || seed.length!=32), "Invalid seed length");
    }

}
