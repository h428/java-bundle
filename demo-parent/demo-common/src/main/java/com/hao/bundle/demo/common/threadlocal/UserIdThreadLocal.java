package com.hao.bundle.demo.common.threadlocal;

public final class UserIdThreadLocal {

    private static final ThreadLocal<Long> LOCAL = new ThreadLocal<>();

    private UserIdThreadLocal() {

    }

    public static void set(Long userId) {
        LOCAL.set(userId);
    }

    public static Long get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }
}
