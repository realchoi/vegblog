package com.realchoi.vegblogboot.util;

import java.security.MessageDigest;

public class Md5Util {
    /**
     * 对字符串进行 MD5 转换
     *
     * @param key 待转换的字符串
     * @return 转换后的 MD5 字符串
     */
    public static String md5(String key) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = key.getBytes();
            // 获得 MD5 摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            char[] str = new char[md.length * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            // logger.error(" 生成 MD5 失败 ", e);
            return "";
        }
    }
}
