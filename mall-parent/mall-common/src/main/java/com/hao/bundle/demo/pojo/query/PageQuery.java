package com.hao.bundle.demo.pojo.query;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class PageQuery {

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 10;

}
