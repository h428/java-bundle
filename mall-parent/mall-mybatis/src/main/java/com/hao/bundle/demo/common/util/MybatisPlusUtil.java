package com.hao.bundle.demo.common.util;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.bundle.demo.pojo.wrapper.PageBean;
import java.util.List;

public class MybatisPlusUtil {

    public static <T> PageBean<T> pageConvert(Page<?> pageMybatis, Class<T> clazz) {
        List<?> records = pageMybatis.getRecords();
        List<T> data = BeanUtil.copyToList(records, clazz);
        return PageBean.<T>builder()
            .pages((int)pageMybatis.getPages())
            .total(pageMybatis.getTotal())
            .list(data)
            .build();
    }

}
