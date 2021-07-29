package com.demo.common.base;

import com.demo.common.mybatis.MyInsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface MyMapper<T> extends Mapper<T>, MyInsertListMapper<T> {


}