package com.hao.bundle.demo.common.util;


import cn.hutool.core.bean.BeanUtil;
import com.hao.bundle.demo.pojo.wrapper.Page;
import java.util.List;

/**
 * Jpa 涉及的工具类
 */
public class JpaUtil {

    public static <T> Page<T> pageConvert(org.springframework.data.domain.Page<?> jpaPage,
        Class<T> clazz) {
        List<T> data = BeanUtil.copyToList(jpaPage.getContent(), clazz);

        return Page.<T>builder()
            .pages(jpaPage.getTotalPages())
            .total(jpaPage.getTotalElements())
            .list(data)
            .build();
    }


}
