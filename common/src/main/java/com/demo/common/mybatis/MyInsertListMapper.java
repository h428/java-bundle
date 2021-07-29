package com.demo.common.mybatis;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.additional.insert.InsertListProvider;

import java.util.List;

@tk.mybatis.mapper.annotation.RegisterMapper
public interface MyInsertListMapper<T> {

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等
     * <p>
     * 不支持主键策略，插入前需要设置好主键的值
     * <p>
     * 特别注意：2018-04-22 后，该方法支持 @KeySql 注解的 genId 方式
     *
     * @param recordList
     * @return
     */
    @InsertProvider(type = InsertListProvider.class, method = "dynamicSQL")
    int insertList(List<? extends T> recordList);

}
