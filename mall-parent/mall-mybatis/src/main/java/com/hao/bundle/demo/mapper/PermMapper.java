package com.hao.bundle.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.bundle.demo.entity.Perm;
import java.util.List;
import org.apache.ibatis.annotations.Select;

public interface PermMapper extends BaseMapper<Perm> {

    @Select("select * from perm where id in (select perm_id from role_perm where role_id = #{roleId})")
    List<Perm> findByRoleId(Long roleId);
}
