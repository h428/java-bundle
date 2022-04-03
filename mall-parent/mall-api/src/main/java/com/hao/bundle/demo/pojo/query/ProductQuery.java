package com.hao.bundle.demo.pojo.query;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class ProductQuery {

    /**
     * 名称，使用 like
     */
    private String name;

    /**
     * 分类 1
     */
    private Long cid1;

    /**
     * 分类 2
     */
    private Long cid2;

}
