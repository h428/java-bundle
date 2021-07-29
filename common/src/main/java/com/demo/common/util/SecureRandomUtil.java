package com.demo.common.util;

import java.security.SecureRandom;

// 安全随机数工具
public class SecureRandomUtil {

    private static SecureRandom rng = new SecureRandom();

    public static long getLong() {
        return rng.nextLong();
    }

    public static String getLongHex() {
        return Long.toHexString(rng.nextLong());
    }
}
