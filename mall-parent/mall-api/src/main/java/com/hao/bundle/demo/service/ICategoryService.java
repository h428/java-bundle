package com.hao.bundle.demo.service;

import com.hao.bundle.demo.pojo.dto.CategoryDto;
import com.hao.bundle.demo.pojo.dto.CategorySaveDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * Service 层参数校验正确写法：在类上打 @Validated 注解，然后在方法参数上打上 @Valid 或其他单个校验注解
 */
@Validated
public interface ICategoryService {

    /**
     * 保存分类，若 id 为空是新增，id 不为空则更新；
     * 非法 id, pid 都会抛出业务异常
     * @param categorySaveDto 分类信息
     */
    void save(@Valid CategorySaveDto categorySaveDto);


    /**
     * 根据 id 删除分类；如果该分类有子分类无法删除，抛出异常
     * @param id 主键
     */
    void delete(Long id);

    /**
     * 根据 id 判断指定记录是否存在
     * @param id 分类 id
     * @return 存在则返回 true，否则 false
     */
    boolean existById(Long id);

    /**
     * 根据 id 查询分类信息
     *
     * @param id 分类 id
     * @return 分类信息
     */
    CategoryDto get(Long id);

    /**
     * 根据 id 查询分类树（递归查询子分类）信息
     *
     * @param id 分类 id
     * @return 分类信息
     */
    CategoryDto getWithChildren(Long id);

    /**
     * 根据 pid 查询下一级子类
     *
     * @param pid 父类 id
     * @return 子类列表
     */
    List<CategoryDto> listChildrenByPid(Long pid);

}
