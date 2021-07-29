package com.demo.common.mapper2;

import com.demo.common.entity.Perm;
import com.demo.common.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface AddUserMapper {

    // 注意所有查询，若不存在返回的都是 size = 0 的空集对象而不是 null

    // 根据 userId 查询拥有的角色集
    @Select("select role.id id, name from role inner join user on role.id = user.role_id where user.id = #{userId} limit 1")
    Role queryRoleByUserId(@Param("userId") Long userId);

    // 根据 roleId 查询权限集对象
    @Select("select id, name, tag, pid, nav from perm inner join role_perm on perm.id = role_perm.perm_id where role_perm.role_id = #{roleId}")
    List<Perm> queryPermListByRoleId(@Param("roleId") Long roleId);

    // 根据 roleId 查询字符串权限集（字符串更方便）
    @Select("select tag from perm inner join role_perm on perm.id = role_perm.perm_id where role_perm.role_id = #{roleId}")
    Set<String> queryStringPermSetByRoleId(@Param("roleId") Long roleId);

    // 根据 roleId 查询权限集 id
//    @Select("select perm.id from perm inner join role_perm on perm.id = role_perm.perm_id where role_perm.role_id = #{roleId} and perm.nav = #{nav}")
    List<Long> queryPermIdListByRoleId(@Param("roleId") Long roleId, @Param("nav") Boolean nav);

    // 给定 pidList，插入所有相关的 perm（包括 pid 自身 perm 以及子 perm）
    int insertPermByPidList(@Param("roleId") Long roleId, @Param("pidList") List<Long> pidList);

    // 直接根据 userId 查询字符串权限集（多表联合） - 为了提供 xml 示例，该方法在 xml 中实现（参考 resource.mappers2 文件）
    Set<String> queryStringPermSetByUserId(@Param("userId") Long userId, @Param("nav") Boolean nav);


}
