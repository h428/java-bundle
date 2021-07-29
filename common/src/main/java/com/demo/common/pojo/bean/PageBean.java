package com.demo.common.pojo.bean;

import com.github.pagehelper.PageInfo;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageBean<T> implements Serializable {

    private int total; // 总记录数
    private int pages; // 总页数
    protected List<T> list;


    public static <T> PageBean<T> fromPageInfo(PageInfo<T> pageInfo) {
        return PageBean.<T>builder()
                .total((int)pageInfo.getTotal())
                .pages(pageInfo.getPages())
                .list(pageInfo.getList())
                .build();
    }

    public static void main(String[] args) {

    }
}
