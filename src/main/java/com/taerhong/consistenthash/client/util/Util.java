package com.taerhong.consistenthash.client.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author billhu
 */
public class Util {

    /**
     * 对字符串hash
     *
     * @param s
     * @return
     */
    public static int hashCodeFor(String s) {

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            System.out.println("不支持MD5");
            return s.hashCode();
        }
        md5.update(s.getBytes());

        byte[] bKey = md5.digest();
        long result = ((long) (bKey[3] & 0xFF) << 24)
                | ((long) (bKey[2] & 0xFF) << 16
                | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF));
        result = result & 0xffffffffL;
        return (int) result;
    }
}
