package com.enterprise.isv.utils;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * PKCS7算法的加密填充
 *
 * @Author shisan
 * @Date 2018/3/26 上午9:51
 */
public class PKCS7Padding {
    private final static Charset CHARSET = Charset.forName("utf-8");
    private final static int BLOCK_SIZE = 32;

    /**
     * 填充mode字节
     *
     * @param count
     * @return
     */
    public static byte[] getPaddingBytes(int count) {
        int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
        if (amountToPad == 0) {
            amountToPad = BLOCK_SIZE;
        }
        char padChr = chr(amountToPad);
        StringBuffer tmpSbf = new StringBuffer(200);
        for (int index = 0; index < amountToPad; index++) {
            tmpSbf.append(padChr);
        }
        return tmpSbf.toString().getBytes(CHARSET);
    }

    /**
     * 移除mode填充字节
     *
     * @param decrypted
     * @return
     */
    public static byte[] removePaddingBytes(byte[] decrypted) {
        int pad = (int) decrypted[decrypted.length - 1];
        if (pad < 1 || pad > BLOCK_SIZE) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    private static char chr(int a) {
        byte target = (byte) (a & 0xFF);
        return (char) target;
    }

}
