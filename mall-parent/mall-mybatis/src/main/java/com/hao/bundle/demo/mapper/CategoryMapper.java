package com.hao.bundle.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.bundle.demo.entity.Category;
import org.apache.ibatis.annotations.Select;

public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 根据 id 判断分类是否存在
     * @param id 分类 id
     * @return 存在返回 true，否则 false
     */
    @Select("select exists(select id from category where id = #{id})")
    boolean existById(Long id);


    /**
     * 根据 pid 判断该分类是否存在子分类
     * @param pid 父分类 id
     * @return 若该父分类存在子分类（子分类的 pid 为指定值）则返回 true，否则返回 false
     */
    @Select("select exists(select id from category where pid = #{pid})")
    boolean existChildrenByPid(Long pid);
}
