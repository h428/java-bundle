package com.hao.bundle.demo.dao;

import com.hao.bundle.demo.common.base.BaseDao;
import com.hao.bundle.demo.entity.Perm;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PermDao extends BaseDao<Perm, Long> {

    /**
     * 自定义查询语句样例，可使用 ?1 或 :roleId 读取参数，nativeQuery 表示是 sql，否则为 jpql
     *
     * @param roleId 角色
     * @return 权限列表
     */
    @Query(value = "select * from perm where id in (select perm_id from role_perm where role_id = :roleId)", nativeQuery = true)
    List<Perm> findByRoleId(Long roleId);

}
