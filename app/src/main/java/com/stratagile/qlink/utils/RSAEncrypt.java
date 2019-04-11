/**
 * @Package com.qlink.common.security
 * @Description
 * @author yifang.huang
 * @date 2019年3月14日 上午11:46:40
 * @version V1.0
 */
package com.stratagile.qlink.utils;

import com.stratagile.qlink.utils.Base64.Base64;
import com.vondear.rxtools.RxEncodeTool;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;

/**
 * @Description RSA 加密
 * @author yifang.huang
 * @date 2019年3月14日 上午11:46:40
 */
public class RSAEncrypt {

    private static final Logger logger = LoggerFactory.getLogger(RSAEncrypt.class);

    public static KeyPairGenerator keyPairGen;

    static {
        try {

            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            keyPairGen = KeyPairGenerator.getInstance("RSA");

            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(1024, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            logger.error("初始化KeyPairGenerator失败", e);
            e.printStackTrace();
        }
    };

    /**
     *
     * @Description 生成密钥对
     * @return Map<Integer,String> 0_公钥, 1_私钥
     * @author yifang.huang
     * @date 2019年3月14日 上午11:57:46
     */
    public static Map<Integer, String> genKeyPair() {

        // 用于封装随机产生的公钥与私钥
        Map<Integer, String> keyMap = new HashMap<Integer, String>();

        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));

        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));

        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString);  //0表示公钥
        keyMap.put(1, privateKeyString);  //1表示私钥

        return keyMap;
    }

    /**
     *
     * @Description 公钥加密
     * @param content 需要加密的字符串
     * @param publicKey 公钥
     * @return String 密文
     * @author yifang.huang
     * @date 2019年3月14日 下午12:00:09
     */
    public static String encrypt(String content, String publicKey) {
        try {
            //base64编码的公钥
            byte[] decoded = RxEncodeTool.base64Decode(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            String outStr = RxEncodeTool.base64Encode2String(cipher.doFinal(content.getBytes("UTF-8")));
            return outStr;
        } catch (Exception e) {
            logger.error("加密失败", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @Description 私钥解密
     * @param content 需要解密的字符串
     * @param privateKey 私钥
     * @return String 铭文
     * @author yifang.huang
     * @date 2019年3月14日 下午12:01:47
     */
    public static String decrypt(String content, String privateKey) {
        try {
            //64位解码加密后的字符串
            byte[] inputByte = RxEncodeTool.base64Decode(content.getBytes("UTF-8"));
            //base64编码的私钥
            byte[] decoded = RxEncodeTool.base64Decode(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            String outStr = new String(cipher.doFinal(inputByte));

            return outStr;
        } catch (Exception e) {
            logger.error("解密失败", e);
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
		//加密字符串
		String message = "1554881300164,25f9e794323b453885f5181f1b624d0b";
		String messageEn = encrypt(message,"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCF4Udd/6e3wjiLrJQG/81NwfUlSLWygnALqoqLtAmCRoewfDZ+/a3B1o4xv9QFW61MIMa0SrmOUsLZXsUWDjWYVzuq1Joo9O4OwKJe6Tz+5kEzubendrttocuvLm/hrsqJ4iDgM37Wb7JhuNZfVWeNtk6GWgj18bAFu3FTthLCcwIDAQAB");
		System.out.println(message + "\t加密后的字符串为:" + messageEn);
		messageEn = "bas49gCxa0xd1ps3yzpqaOfPj1Gj/fRg7bYPkUximwYWBPWuPpPhRQUXxsPzB7mP3n66UFvcTqIPKs/3m6TFNDgj+qPxUjE94KEULe9EixmoQJjZpAH2UzE6KtTIalandNxz6UZXwaSXg0NQT2bEkdjBxVrZAt7XCHeRuS2iY5s=";
		String messageDe = decrypt(messageEn,"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIXhR13/p7fCOIuslAb/zU3B9SVItbKCcAuqiou0CYJGh7B8Nn79rcHWjjG/1AVbrUwgxrRKuY5SwtlexRYONZhXO6rUmij07g7Aol7pPP7mQTO5t6d2u22hy68ub+GuyoniIOAzftZvsmG41l9VZ422ToZaCPXxsAW7cVO2EsJzAgMBAAECgYA9rTsjotOxZFiIgEjxsIb0e5ZkRsruIglcVoTdN2PqEHQSaibw+g1Cb4WyhZ03mrSLjc384S/60UXSvkYtkv49M7AdyftUWwNitK6ascjmYY4R01GWC7D3ZAaQxtM5K8GKTOTitQPXBl0FEkDNJGiVmP3rcO5Wxac2PKcDrNqLAQJBANk4WJZipzFh07mUhUcmcM+igSrDUAMcLCuyeMnmQARc+vItgd4LJuMMFPO9gZhJKzDOqmZ36alNbqXqlJabjEkCQQCdyAXp5Dj+XFwydLCF+ZbRZJqrakpzbU4JLUAMDNHV4ri8ROgV57aEebRsl/1N9d8vL8d5ysanGLADgZapc8DbAkAtTH3U5r/dIXyIz/s3SkHuWwI6y75M17wyZKah3B1vi4BdrrXNe1/hq2xXJCb5fhC+vep1Mf6NavNvMEtKWSfZAkEAh8WW6SS9sowxvi1RtYgIMxmaSwVFGbymWRk6MuRZMO9PPpshB7CEC81a59OGYq7AJi+8PF60wRdqZyn9RsXX3wJAEkIC/p1foWi6EtT79pQo9cBBovD6ggWsP0dnMqZkPkNKy1nhtiz99hnQ2jB97WvDvnXoJDUy2dCx81z8SEBKaQ==");
		System.out.println("还原后的字符串为:" + messageDe);
        //System.out.println(encrypt("12345678", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCdtaPVWHiIGyxq9dQu8EUrWE3GqtLTCls/b3mrJXoyZATJNZuNiTqS1AbA6LUOKIlTUJDSWH7B6rOHUdzmsGOAbCAP648wSGiPTrqQXtsLMSKc+BoPggHLUOdAVrh2yVdGGO0/7D4SfZ8lPqAXJajEmWodYqt6Wyptx3TZMTu2wIDAQAB"));
    }

}
