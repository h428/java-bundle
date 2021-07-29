package com.demo.boot.ssm.bean.thredlocal;

public class UserIdThreadLocal {

    private static final ThreadLocal<Long> LOCAL = new ThreadLocal<>();

    private UserIdThreadLocal() {

    }

    public static void set(Long userId) {
        LOCAL.set(userId);
    }

    public static Long get() {
        return LOCAL.get();
    }
}
