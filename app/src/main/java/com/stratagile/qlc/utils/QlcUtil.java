package com.stratagile.qlc.utils;

/*
  Utilities for crypto functions
 */


import org.libsodium.jni.NaCl;
import org.libsodium.jni.Sodium;

import java.math.BigInteger;
import java.security.SecureRandom;


public class QlcUtil {
    private final static char[] hexArray = "0123456789abcdef".toCharArray();
    public final static String addressCodeArray = "13456789abcdefghijkmnopqrstuwxyz";
    public final static char[] addressCodeCharArray = addressCodeArray.toCharArray();


    /**
     * Generate a new Wallet Seed
     *
     * @return Wallet Seed
     */
    public static String generateSeed() {
        int numchars = 64;
        SecureRandom random = SecureRandomUtil.secureRandom();
        byte[] randomBytes = new byte[numchars / 2];
        random.nextBytes(randomBytes);

        StringBuilder sb = new StringBuilder(numchars);
        for (byte b : randomBytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }



    /**
     * Convert a private key to a public key
     *
     * @param private_key private key
     * @return public key
     */
    public static String privateToPublic(String private_key) {
        Sodium sodium = NaCl.sodium();
        byte[] public_key_b = new byte[Sodium.crypto_generichash_blake2b_bytes()];
        byte[] private_key_b = hexToBytes(private_key);

        Sodium.crypto_sign_ed25519_sk_to_pk(public_key_b, private_key_b);

        return bytesToHex(public_key_b);
    }

    /**
     * Sign a message with a private key
     *
     * @param private_key Private Key
     * @param data        Message
     * @return Signed message
     */
    public static String sign(String private_key, String data) {
        Sodium sodium = NaCl.sodium();
        byte[] data_b = hexToBytes(data);
        byte[] private_key_b = hexToBytes(private_key);

        byte[] signature = new byte[Sodium.crypto_sign_bytes()];
        System.out.println(signature.length);
        int[] signature_len = new int[1];
        Sodium.crypto_sign_ed25519_detached(signature, signature_len, data_b, data_b.length, private_key_b);
        return bytesToHex(signature);
    }

    /**
     * Convert a Public Key to an Address
     *
     * @param public_key Public Key
     * @return xrb address
     */
    public static String publicToAddress(String public_key) {
        Sodium sodium = NaCl.sodium();
        byte[] bytePublic = QlcUtil.hexStringToByteArray(public_key);
        String encodedAddress = encode(public_key);

        byte[] state = new byte[Sodium.crypto_generichash_statebytes()];
        byte[] key = new byte[Sodium.crypto_generichash_keybytes()];
        byte[] check_b = new byte[5];

        Sodium.crypto_generichash_blake2b_init(state, key, 0, 5);
        Sodium.crypto_generichash_blake2b_update(state, bytePublic, bytePublic.length);
        Sodium.crypto_generichash_blake2b_final(state, check_b, check_b.length);

        reverse(check_b);

        StringBuilder resultAddress = new StringBuilder();
        resultAddress.insert(0, "qlc_");
        resultAddress.append(encodedAddress);
        resultAddress.append(encode(QlcUtil.bytesToHex(check_b)));

        return resultAddress.toString();

    }


    /**
     * Convert an address to a public key
     *
     * @param encoded_address encoded Address
     * @return Public Key
     */
    public static String addressToPublic(String encoded_address) {
        NaCl.sodium();
        String data = encoded_address.split("_")[1].substring(0, 52);
        byte[] data_b = QlcUtil.hexStringToByteArray(decodeAddressCharacters(data));
        byte[] state = new byte[Sodium.crypto_generichash_statebytes()];
        byte[] key = new byte[Sodium.crypto_generichash_keybytes()];
        byte[] verify_b = new byte[5];

        Sodium.crypto_generichash_blake2b_init(state, key, 0, 5);
        Sodium.crypto_generichash_blake2b_update(state, data_b, data_b.length);
        Sodium.crypto_generichash_blake2b_final(state, verify_b, verify_b.length);
        reverse(verify_b);

        // left pad byte array with zeros
        StringBuilder pk = new StringBuilder(QlcUtil.bytesToHex(data_b));
        while (pk.length() < 64) {
            pk.insert(0, "0");
        }

        return pk.toString();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] bigInttoBytes(BigInteger bigInteger) {
        byte[] tmp = bigInteger.toByteArray();
        byte[] res = new byte[tmp.length - 1];
        if (tmp[0] == 0) {
            System.arraycopy(tmp, 1, res, 0, res.length);
            return res;
        }

        return tmp;
    }

    public static byte[] hexToBytes(String hex) {
        hex = hex.length() % 2 != 0 ? "0" + hex : hex;

        byte[] b = new byte[hex.length() / 2];

        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(hex.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        // length of string must be divisible by 2
        if (len % 2 > 0) {
            s = "0" + s;
            len = s.length();
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static void reverse(byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    private static String encode(String hex_data) {
        StringBuilder bits = new StringBuilder();
        bits.insert(0, new BigInteger(hex_data, 16).toString(2));
        while (bits.length() < hex_data.length() * 4) {
            bits.insert(0, '0');
        }

        StringBuilder data = new StringBuilder();
        data.insert(0, bits.toString());
        while (data.length() % 5 != 0) {
            data.insert(0, '0');
        }

        StringBuilder output = new StringBuilder();
        int slice = data.length() / 5;
        for (int this_slice = 0; this_slice < slice; this_slice++) {
            output.append(addressCodeCharArray[Integer.parseInt(data.substring(this_slice * 5, this_slice * 5 + 5), 2)]);
        }
        return output.toString();
    }

    public static String decodeAddressCharacters(String data) {
        StringBuilder bits = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            int index = addressCodeArray.indexOf(data.substring(i, i + 1).charAt(0));
            bits.append(Integer.toBinaryString(0x20 | index).substring(1));
        }
        return new BigInteger(bits.toString(), 2).toString(16);
    }

    public static void main(String[] args) {

    }

}
