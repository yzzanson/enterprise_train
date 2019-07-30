package com.enterprise.base.convert;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AES {
    private static final Logger logger = LoggerFactory.getLogger(AES.class.getCanonicalName());

    public static String AESKEY  = "qwertyuioASD@#!$" ;

    // 加密
    public static String Encrypt(String sSrc, String sKey) {
        try {
            if (sKey == null) {
                logger.warn("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                logger.warn("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");     //"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            return new Base64().encodeToString(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("aes.encrypt is error:" + e.getMessage());
            return null;
        }
    }

    // 解密
    public static String Decrypt(String sSrc, String sKey) {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                logger.warn("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                logger.warn("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);      //先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                logger.warn(e.toString());
                return null;
            }
        } catch (Exception ex) {
            logger.warn(ex.toString());
            ex.printStackTrace();
            return null;
        }
    }

}

