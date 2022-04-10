package com.hao.bundle.demo.pojo.query;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class SortQuery {

    public static final char DESC_SIGN = '-';
    public static final char ASC_SIGN = '+';

    /**
     * 排序字段，格式为 -createTime,+email
     * - 表示降序，+ 表示升序，不写符号默认升序
     */
    @ApiParam("排序字段")
    private String sort;

    /**
     * 排序项
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    @Builder
    public static class SortItem {

        /**
         * 排序属性
         */
        private String property;

        /**
         * 是否升序
         */
        private boolean asc;
    }

    /**
     * 将格式为 "-name, +createTime" 排序文本字段转化为排序项列表
     * @return 排序项列表
     */
    public List<SortItem> toSortItemList() {

        // sort 字符串为空，直接返回空集
        if (StrUtil.isBlank(sort)) {
            return Lists.newArrayList();
        }

        String[] splits = sort.split(",");

        List<SortItem> sortItemList = Lists.newArrayList();

        for (String split : splits) {

            if (StrUtil.isBlank(split)) {
                continue;
            }

            // 取出两端空格，以防在分隔符 , 前后有空格，例如："-name, +createTime"
            split = split.trim();

            char firstCh = split.charAt(0);

            SortItem sortItem;

            switch (firstCh) {
                // 降序排序
                case DESC_SIGN:
                    sortItem = SortItem.builder()
                        .property(split.substring(1))
                        .asc(false)
                        .build();
                    break;

                // 升序排序
                case ASC_SIGN:
                    sortItem = SortItem.builder()
                        .property(split.substring(1))
                        .asc(true)
                        .build();
                    break;

                // 升序排序不带符号
                default:
                    sortItem = SortItem.builder()
                        .property(split)
                        .asc(true)
                        .build();
            }

            sortItemList.add(sortItem);
        }

        return sortItemList;

    }

    public static void main(String[] args) {
        SortQuery sortQuery = SortQuery.builder().sort("-name, +createTime, id").build();
        System.out.println(sortQuery.toSortItemList());
    }

}
