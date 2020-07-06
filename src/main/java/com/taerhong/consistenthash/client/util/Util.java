package com.taerhong.consistenthash.client.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author billhu
 */
public class Util {
    public static final double DIVISOR = Math.pow(2, 32);

    /**
     * 对字符串hash，取模2^32
     * @param s
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static long hashCodeFor(String s) {

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        }catch (NoSuchAlgorithmException noSuchAlgorithmException){
            System.out.println("不支持MD5");
            return s.hashCode();
        }
        md5.update(s.getBytes());
        byte[] res = md5.digest();
        BigInteger bigInteger = new BigInteger(1, res);

        return Math.floorMod(bigInteger.longValue(), (long) DIVISOR);
    }
}
