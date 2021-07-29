package com.demo.common.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.demo.common.exception.InsertErrorException;
import com.demo.common.exception.UpdateErrorException;
import com.demo.common.pojo.converter.PojoConverter;
import com.demo.common.util.ClassUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.*;

@Transactional(readOnly = true)
public abstract class BaseServiceWithDeleted<T> {

    @Autowired
    protected MyMapper<T> mapper;

    protected Class<T> entityClass;

    protected Set<String> orderBySet = Sets.newHashSet("id");

    public BaseServiceWithDeleted() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        // 初始化 orderBySet
        Set<String> orderBySet = this.initOrderBySet();
        if (orderBySet != null) {
            this.orderBySet = orderBySet;
        }
    }

    protected Set<String> initOrderBySet() {
        return null; // 空实现
    }


    /**
     * 为实体设置 deleted
     * @param entity 实体
     * @param deleted deleted 不为 null 则设置
     */
    protected void setDeleted(T entity, Boolean deleted) {
        // 若 deleted 不为 null 则加上 deleted 条件
        if (deleted != null) {
            ClassUtil.setValue(entity, "deleted", deleted);
        }
    }

    /**
     * 根据 example 做分页查询
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param example  查询条件
     * @return 分页对象
     */
    protected PageInfo<T> listPageByExample(int pageNum, int pageSize, Example example) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(this.mapper.selectByExample(example));
    }

    /**
     * 根据 bean 的属性转化为对应的 example
     *
     * @param bean    查询条件 bean
     * @param orderBy 排序列
     * @param desc    是否降序
     * @return 返回 Example
     */
    protected Example beanToExample(Object bean, String orderBy, boolean desc, Boolean deleted) {
        Example example = new Example(entityClass);

        Example.Criteria criteria = example.createCriteria();

        if (orderBySet.contains(orderBy)) {
            example.setOrderByClause(" " + orderBy + (desc ? " desc" : " asc"));
        }

        // 将实体转化为 map，然后移除 map 中的空值
        Map<String, Object> map = MapUtil.removeNullValue(BeanUtil.beanToMap(bean));

        // 设置 deleted 属性
        if (deleted != null) {
            map.put("deleted", deleted);
        }

        criteria.andAllEqualTo(map);

        return example;
    }

    protected T newFakeDeleteEntity() {
        // 构造新实体
        T entity = ClassUtil.newInstance(entityClass);

        // 设置 delTime
        ClassUtil.setValue(entityClass, entity, "delTime", System.currentTimeMillis());

        // 设置 deleted
        ClassUtil.setValue(entityClass, entity, "deleted", true);

        return entity;
    }

    ///////////// public method

    /**
     * 根据 id 查询数据
     *
     * @param id 主键
     * @param deleted 删除标记
     * @return 实体
     */
    public T getById(Object id, Boolean deleted) {

        Example example = new Example(entityClass);

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("id", id);

        if (deleted != null) {
            criteria.andEqualTo("deleted", deleted);
        }

        return this.mapper.selectOneByExample(example);
    }

    /**
     * 根据多个 id 查询对应的实体列表（id 名称必须为 id）
     *
     * @param ids   id 列表
     * @param deleted 删除标记
     * @return ids 对应的实体列表
     */
    public List<T> listByIds(Collection<?> ids, Boolean deleted) {
        Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria();

        // 设置 ids
        criteria.andIn("id", ids);

        // 设置删除标记
        if (deleted != null) {
            criteria.andEqualTo("deleted", deleted);
        }

        return mapper.selectByExample(example);
    }

    /**
     * 查询所有数据
     *
     * @param deleted 删除标记
     * @return 所有数据实体
     */
    public List<T> listAll(Boolean deleted) {
        Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria();

        // 设置删除标记
        if (deleted != null) {
            criteria.andEqualTo("deleted", deleted);
        }

        return this.mapper.selectByExample(example);
    }

    /**
     * 根据条件查询一条数据，如果有多条数据会抛出异常
     *
     * @param record 查询条件
     * @param deleted 删除标记
     * @return 查到的实体，不存在返回 null
     */
    public T getOneByEntity(T record, Boolean deleted) {
        // 设置 deleted 条件
        setDeleted(record, deleted);

        return this.mapper.selectOne(record);
    }

    /**
     * 根据查询条件查询记录数量
     *
     * @param record 条件
     * @param deleted 删除标记
     * @return 数量
     */
    public int countByEntity(T record, Boolean deleted) {
        // 设置 deleted 条件
        setDeleted(record, deleted);

        return mapper.selectCount(record);
    }

    /**
     * 根据条件查询数据列表
     *
     * @param record 查询条件
     * @param deleted 删除标记
     * @return 满足条件的实体列表
     */
    public List<T> listObjectsByEntity(T record, Boolean deleted) {
        // 设置 deleted 条件
        setDeleted(record, deleted);

        return this.mapper.select(record);
    }

    /**
     * 分页查询
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param record   查询条件
     * @param orderBy 排序字段
     * @param desc 是否降序
     * @param deleted 删除标记
     * @return 分页实体 PageInfo，包含了分页信息
     */
    public PageInfo<T> listPageByEntity(int pageNum, int pageSize, T record, String orderBy, boolean desc, Boolean deleted) {
        Example example = this.beanToExample(record, orderBy, desc, deleted);
        return listPageByExample(pageNum, pageSize, example);
    }

    /**
     * 根据 DTO 做分页查询
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param queryDTO 查询条件，最后会转化为实体 T 并调用 listPageByEntity 进行查询
     * @param <DTO>    分页实体 PageInfo，包含了分页信息
     * @param orderBy 排序字段
     * @param desc 是否降序
     * @param deleted 删除标记
     * @return 分页对象
     */
    public <DTO extends PojoConverter<DTO, T>> PageInfo<T> listPageByQueryDTO(int pageNum, int pageSize, DTO queryDTO, String orderBy, boolean desc, Boolean deleted) {
        return this.listPageByEntity(pageNum, pageSize, queryDTO.convertToEntity(), orderBy, desc, deleted);
    }


    /**
     * 根据姓名分页查询，支持通配，但实体必须要有 name 属性，否则失败
     *
     * @param pageNum  页码，注意分页插件页码从 1 而不是 0 开始
     * @param pageSize 每页数量
     * @param name     姓名
     * @return 分页实体 PageInfo，包含了分页信息
     */
    public PageInfo<T> listPageByName(int pageNum, int pageSize, String name, String orderBy, boolean desc, Boolean deleted) {
        PageHelper.startPage(pageNum, pageSize);

        Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(name)) {
            criteria.andLike("name", "%" + name + "%");
        }

        if (deleted != null) {
            criteria.andEqualTo("deleted", deleted);
        }

        // 添加排序字段
        if (this.orderBySet.contains(orderBy)) {
            example.setOrderByClause(orderBy + (desc ? " desc" : " asc"));
        }

        List<T> recordList = this.mapper.selectByExample(example);

        return new PageInfo<>(recordList);
    }

    /**
     * 执行 insert 之前的唯一性校验，一般用于执行 insert 之前的检查，各个子类自己实现，不校验则直接返回 null 即可
     * 一般和数据库的唯一索引语义保持一致（如果数据库没有设置唯一索引，没有的话则做语义上的唯一性校验）
     * 校验后，若想直接响应到 Web，可以直接抛出 InsertErrorException
     *
     * @param entity 实体
     * @return 校验通过则返回 null，校验不通过则返回提示消息，默认直接通过，即不做校验
     */
    public String queryUniqueBeforeSave(T entity) {
        return null;
    }


    /**
     * 执行 update 之前的唯一性校验，一般用于执行 update 之前的检查，各个子类自己实现，不校验则直接返回 null 即可
     * 一般和数据库的唯一索引语义保持一致（如果数据库没有设置唯一索引，没有的话则做语义上的唯一性校验）
     * 校验后，若想直接响应到 Web，可以直接抛出 UpdateErrorException
     *
     * @param entity 实体
     * @return 校验通过则返回 null，校验不通过则返回提示消息，默认直接通过，即不做校验
     */
    public String queryUniqueBeforeUpdate(T entity) {
        return null;
    }


    /**
     * 执行 delete 之前的逻辑性校验，一般用于执行 delete 之前的检查，各个子类自己实现，不校验则直接返回 null 即可
     *
     * @param id 待删除的 id
     * @return 校验通过则返回 null，校验不通过则返回提示消息，默认直接通过，即不做校验
     */
    public String queryCheckBeforeDelete(Object id) {
        return null;
    }

    /////////////// 增删改

    /**
     * 保存解析结果中的数据，注意 id 已经设置
     *
     * @param parseResult 解析结果对象
     */
    @Transactional
    public void saveParseResult(BaseExcelParser.ParseResult<T> parseResult) {

        // todo 思考下改成insertList 形式
        int i = 0;
        for (T entity : parseResult.getDataList()) {

            ++i; // 第 i 条记录

            String msg = this.queryUniqueBeforeSave(entity);

            if (msg != null) {
                throw new InsertErrorException(String.format("插入失败，第 %d 条记录有误，%s", i, msg));
            }

            this.mapper.insertSelective(entity);
        }

    }

    /**
     * 新增数据，使用不为 null 的字段，返回成功的条数
     *
     * @param record 新增的数据
     * @return 成功条数
     */
    @Transactional
    public int saveSelective(T record) {

        String msg = this.queryUniqueBeforeSave(record);
        if (msg != null) {
            throw new InsertErrorException(msg);
        }
        return this.mapper.insertSelective(record);
    }

    /**
     * 批量插入不对动态判断 null 列，会使用所有属性列，
     * 因此所有列都要符合数据库的约束，不能为 null 的必须要有值
     * 如果数据库允许 null 则实体类的对应字段也允许 null
     * @param recordList 实体列表，底层使用 insert into table values (...), (...), (...);
     * @return 成功插入的行数
     */
    @Transactional
    public int saveList(List<T> recordList) {

        // 插入前的校验
        int i = 0;

        for (T entity : recordList) {
            String msg = this.queryUniqueBeforeSave(entity);
            if (msg != null) {
                throw new InsertErrorException("第 " + (++i) + "条记录有误 : " + msg);
            }
        }

        return this.mapper.insertList(recordList);
    }

    /**
     * 修改数据，使用不为 null 的字段，返回成功的条数
     *
     * @param record 修改数据
     * @return 修改成功的条数
     */
    @Transactional
    public int updateSelectiveById(T record) {
        String msg = this.queryUniqueBeforeUpdate(record);
        if (msg != null) {
            throw new UpdateErrorException(msg);
        }
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据 id 物理删除数据
     *
     * @param id 待删除的数据 id
     * @return 删除成功的记录数
     */
    @Transactional
    public int deleteById(Object id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 给定多个 id 批量删除，property 填 id 名称
     *
     * @param property 数据库中的主键名，一般为 id
     * @param ids      ids 的值
     * @return 删除成功的记录数
     */
    @Transactional
    public int deleteByIds(String property, List<Object> ids) {
        Example example = new Example(entityClass);
        example.createCriteria().andIn(property, ids);
        return this.mapper.deleteByExample(example);
    }

    /**
     * 根据条件删除
     *
     * @param record 删除条件
     * @return 删除成功的记录数
     */
    @Transactional
    public int deleteByEntity(T record) {
        return this.mapper.delete(record);
    }

    /**
     * 根据 id 伪删除
     *
     * @param id 主键
     * @return 成功删除的行数
     */
    @Transactional
    public int fakeDeleteById(Object id) {
        T entity = newFakeDeleteEntity();

        // 设置 id
        ClassUtil.setValue(entity, "id", id);

        // 执行根据 id 更新
        return this.mapper.updateByPrimaryKeySelective(entity);
    }

    @Transactional
    public int fakeDeleteByIds(List<Object> ids) {

        // 删除删除实体
        T entity = newFakeDeleteEntity();

        Example example = new Example(entityClass);
        example.createCriteria()
                .andIn("id", ids);


        // 执行根据条件更新
        return this.mapper.updateByExampleSelective(entity, example);
    }

    @Transactional
    public int fakeDeleteByEntity(T record) {

        // 删除删除实体
        T entity = newFakeDeleteEntity();

        // 将查询实体转化为 Example
        Example example = beanToExample(record, null, false, null);

        // 执行根据条件更新更新
        return this.mapper.updateByExampleSelective(entity, example);
    }

}
