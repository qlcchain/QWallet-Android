package com.stratagile.qlink.utils;
//
//  https://github.com/WelkinXie/AESCipher-Java
//

import com.vondear.rxtools.RxEncodeTool;


import org.bouncycastle.util.encoders.Hex;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;

public class AESCipher {

    private static final String IV_STRING = "AABBCCDDEEFFGGHH";
    private static final String charset = "UTF-8";
    private static String key = "winqseedwinqseed";

    public static String aesEncryptString(String content) {
        byte[] contentBytes = new byte[0];
        try {
            contentBytes = content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = key.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] encryptedBytes = new byte[0];
        try {
            encryptedBytes = aesEncryptBytes(contentBytes, keyBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        return RxEncodeTool.base64Encode2String(encryptedBytes);
        return Hex.toHexString(encryptedBytes);
    }

    public static String aesDecryptString(String content) {

        byte[] encryptedBytes = RxEncodeTool.base64Decode(content);
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = key.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] decryptedBytes = new byte[0];
        try {
            decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            return new String(decryptedBytes, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] aesDecryptByte(String content, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] encryptedBytes = RxEncodeTool.base64Decode(content);
        byte[] keyBytes = key.getBytes(charset);
        byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes);
        return decryptedBytes;
    }

    public static byte[] aesEncryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
    }

    public static byte[] aesDecryptBytes(byte[] contentBytes, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
    }

    private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        byte[] initParam = IV_STRING.getBytes(charset);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivParameterSpec);
        byte[] result = cipher.doFinal(contentBytes);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(aesEncryptString("123456789"));
        System.out.println(aesDecryptString("5f6f7ccf5beff176f31b99f774fd9270"));
        System.out.println(aesDecryptString("5f6f7ccf5beff176f31b99f774fd9270"));
    }

}
