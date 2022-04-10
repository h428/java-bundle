package com.hao.bundle.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.bundle.demo.entity.Perm;
import java.util.List;
import org.apache.ibatis.annotations.Select;

public interface PermMapper extends BaseMapper<Perm> {

    /**
     * 根据 roleId 查询该角色的权限列表
     * @param roleId 角色 id
     * @return 权限列表
     */
    @Select("select * from perm where id in (select perm_id from role_perm where role_id = #{roleId})")
    List<Perm> findByRoleId(Long roleId);
}
