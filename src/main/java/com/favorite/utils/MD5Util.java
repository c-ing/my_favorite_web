package com.favorite.utils;

import com.favorite.domain.result.ExceptionMsg;

import java.security.MessageDigest;

/**
 * Created by cdc on 2018/6/21.
 */
public class MD5Util {

    public static String encrypt(String dataStr) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(dataStr.getBytes("UTF-8"));
            byte[] s = messageDigest.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00)
                        .substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
