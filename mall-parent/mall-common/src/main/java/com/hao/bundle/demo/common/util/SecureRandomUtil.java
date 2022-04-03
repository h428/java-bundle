package com.hao.bundle.demo.common.util;

import java.security.SecureRandom;

/**
 * 安全随机数工具
 */
public class SecureRandomUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static long getLong() {
        return secureRandom.nextLong();
    }

    public static String getLongHex() {
        return Long.toHexString(secureRandom.nextLong());
    }
}
