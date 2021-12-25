package com.hao.bundle.demo.common.component;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.hao.bundle.demo.common.constant.TimeConstant;

/**
 * 通用 token 工具，使用 Redis 模拟 Session 来存储 token
 */
public class TokenUtil {

    public static final String USER_TOKEN_PREFIX = "USER:";
    public static final String ADMIN_TOKEN_PREFIX = "ADMIN:";

    /**
     * 生成 token，保存到 redis 并返回
     *
     * @param prefix 保存到 redis 的前缀
     * @param userId 生成 token 的用户 id
     * @return token
     */
    private static String generateLoginToken(String prefix, Long userId) {
        Assert.notNull(userId);

        // 使用 UUID 作为 token
        String token = IdUtil.simpleUUID();

        // 随机生成 key 作为 token
        String key;
        do {
            key = prefix + token;
        } while (RedisUtil.exist(key));

        // 保存 token 到 redis
        RedisUtil.put(key, String.valueOf(userId), TimeConstant.SECONDS_OF_ONE_HOUR);

        return token;
    }

    public static String generateTokenOfBaseUser(Long baseUserId) {
        return generateLoginToken(USER_TOKEN_PREFIX, baseUserId);
    }

    public static String generateTokenOfAdmin(Long adminId) {
        return generateLoginToken(ADMIN_TOKEN_PREFIX, adminId);
    }

}
