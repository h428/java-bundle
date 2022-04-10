package com.hao.bundle.demo.common.util;


import cn.hutool.core.bean.BeanUtil;
import com.hao.bundle.demo.pojo.query.SortQuery;
import com.hao.bundle.demo.pojo.query.SortQuery.SortItem;
import com.hao.bundle.demo.pojo.wrapper.PageBean;
import java.util.List;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

/**
 * Jpa 涉及的工具类
 */
public class JpaUtil {

    /**
     * 将 Hibernate 的 Page 对象转化为自定义的 PageBean 对象，注意会触发属性拷贝并转换类型（copyProperties）
     * @param jpaPage hibernate 的分页对象
     * @param clazz 目标类
     * @param <T> 要转化的目标类泛型
     * @return
     */
    public static <T> PageBean<T> pageConvert(Page<?> jpaPage,
        Class<T> clazz) {
        List<T> data = BeanUtil.copyToList(jpaPage.getContent(), clazz);

        return PageBean.<T>builder()
            .pages(jpaPage.getTotalPages())
            .total(jpaPage.getTotalElements())
            .list(data)
            .build();
    }

    /**
     * 将自定义的 SortQuery 对象转化为 Hibernate 的 Sort 以及 Sort.Order 对象
     * @param sortQuery 待转换的 SortQuery 对象
     * @return 转换后的 Hibernate 排序对象
     */
    public static Sort sortConvert(SortQuery sortQuery) {

        if (sortQuery == null) {
            return Sort.unsorted();
        }

        List<SortItem> sortItemList = sortQuery.toSortItemList();

        List<Sort.Order> orderList = Lists.newArrayList();
        for (SortItem sortItem : sortItemList) {
            if (sortItem.isAsc()) {
                orderList.add(Order.asc(sortItem.getProperty()));
                continue;
            }
            orderList.add(Order.desc(sortItem.getProperty()));
        }

        return Sort.by(orderList);
    }


}
