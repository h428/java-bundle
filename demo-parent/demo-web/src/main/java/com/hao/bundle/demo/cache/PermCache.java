package com.hao.bundle.demo.cache;

import cn.hutool.core.util.StrUtil;
import com.hao.bundle.demo.common.component.RedisUtil;
import com.hao.bundle.demo.common.constant.TimeConstant;
import com.hao.bundle.demo.common.util.SpringContextUtil;
import com.hao.bundle.demo.pojo.dto.PermDto;
import com.hao.bundle.demo.pojo.dto.RoleDto;
import com.hao.bundle.demo.pojo.dto.UserDto;
import com.hao.bundle.demo.service.IRoleService;
import com.hao.bundle.demo.service.IUserService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限模块的缓存
 */
public class PermCache {

    private static IRoleService roleService;
    private static IUserService userService;

    // 缓存报销时的秒数
    private static long PERM_CACHE_TIME = TimeConstant.SECONDS_OF_HALF_HOUR;

    static {
        roleService = SpringContextUtil.getBean(IRoleService.class);
        userService = SpringContextUtil.getBean(IUserService.class);
    }

    private static final String USER_ROLE_PREFIX = "USER_ROLE:";

    public static boolean userHasPerm(Long userId, String perm) {

        final String key = USER_ROLE_PREFIX + userId;

        String roleIdStr = RedisUtil.get(key);

        if (StrUtil.isEmpty(roleIdStr)) {
            UserDto userDto = userService.get(userId);

            if (userDto == null) return false;

            // 特别注意，Redis 采用 json 序列化，直接保存 Long 类型将无法反序列化为 Long，故转为 String
            roleIdStr = userDto.getRoleId() + "";
            RedisUtil.put(key, roleIdStr, PERM_CACHE_TIME);
        }

        Long roleId = Long.valueOf(roleIdStr);

        return roleHasPerm(roleId, perm);
    }

    private static final String PERM_PREFIX = "PERM:";

    public static boolean roleHasPerm(Long roleId, String perm) {

        final String key = PERM_PREFIX + roleId;

        if (!RedisUtil.exist(key)) {
            initRolePerm(roleId);
        }

        return RedisUtil.hashExist(key, perm);
    }

    public static void initRolePerm(Long roleId) {
        RoleDto roleDto = roleService.get(roleId);
        final String key = PERM_PREFIX + roleId;
        List<String> list = roleDto.getPermList().stream().map(PermDto::getTag).collect(Collectors.toList());
        if (list.isEmpty()) {
            RedisUtil.hashPut(key, "none", "1", PERM_CACHE_TIME);
            return;
        }
        list.forEach(perm -> RedisUtil.hashPut(key, perm, "1", PERM_CACHE_TIME));
    }

}
