package qlc.utils;

import qlc.utils.Base64.Base64;

public class Encodes {

	// Base64 encode
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}
	

	// Base64 encode, URL safe(change '+' and '/' to '-'and'_', RFC3548)
	public static String encodeUrlSafeBase64(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	// Base64 decode
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}

}
