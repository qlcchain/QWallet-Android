package com.stratagile.qlink.utils;

import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;


public class AESUtil {
    private static String src = "TestAES";
    private static String key = "winqseed";

    public static void jdkAES() {
        try {
            //生成Key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//            keyGenerator.init(128);
//            keyGenerator.init(128, new SecureRandom("seedseedseed".getBytes()));
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(key.getBytes());
            keyGenerator.init(128, random);
            //使用上面这种初始化方法可以特定种子来生成密钥，这样加密后的密文是唯一固定的。
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            //Key转换
            Key key = new SecretKeySpec(keyBytes, "AES");

            //加密
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodeResult = cipher.doFinal(AESUtil.src.getBytes());
            System.out.println("AESencode : " + Hex.toHexString(encodeResult));

            //解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodeResult = cipher.doFinal(encodeResult);
            System.out.println("AESdecode : " + new String(decodeResult));


        } catch (NoSuchAlgorithmException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

    }

    public static String encode(String res) {
        try {
            //生成Key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//            keyGenerator.init(128);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(key.getBytes());
            keyGenerator.init(128, random);
            //使用上面这种初始化方法可以特定种子来生成密钥，这样加密后的密文是唯一固定的。
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            //Key转换
            Key key = new SecretKeySpec(keyBytes, "AES");

            //加密
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodeResult = cipher.doFinal(res.getBytes());
            System.out.println("AESencode : " + Hex.toHexString(encodeResult));
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] xxx = Hex.toHexString(encodeResult).getBytes();
            byte[] decodeResult = cipher.doFinal(Hex.toHexString(encodeResult).getBytes());
            System.out.println("AESdecode : " + Hex.toHexString(decodeResult));
            return Hex.toHexString(encodeResult);


        } catch (NoSuchAlgorithmException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return "";
    }

    public static String decode(String encodeResult) {
        try {
            //生成Key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//            keyGenerator.init(128);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(key.getBytes());
            keyGenerator.init(128, random);
            //使用上面这种初始化方法可以特定种子来生成密钥，这样加密后的密文是唯一固定的。
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            Key key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            //解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodeResult = cipher.doFinal(encodeResult.getBytes());
            System.out.println("AESdecode : " + Hex.toHexString(decodeResult));
            return new String(decodeResult);
        } catch (NoSuchAlgorithmException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return "";
    }


    public static void bcAES() {
        try {

            //使用BouncyCastle 的DES加密
            Security.addProvider(new BouncyCastleProvider());

            //生成Key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "BC");
            keyGenerator.getProvider();
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            //Key转换
            Key key = new SecretKeySpec(keyBytes, "AES");

            //加密
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodeResult = cipher.doFinal(AESUtil.src.getBytes());
            System.out.println("AESencode : " + Hex.toHexString(encodeResult));

            //解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodeResult = cipher.doFinal(encodeResult);
            System.out.println("AESdecode : " + new String(decodeResult));


        } catch (NoSuchAlgorithmException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        jdkAES();
        System.out.println(encode("123456789"));
        System.out.println(decode("f7489eb73c28023d736351c9ecf92490"));
//        System.out.println(decode("fddba40569c81c87c30bd05b917116e7"));
//        System.out.println(decode("b40483e8dbb19a84308d8ebf4337ac42"));
    }
}
