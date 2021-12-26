package com.hao.bundle.demo.pojo.wrapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString
@ApiModel("分页结果")
public class Page<T> implements Serializable {

    @ApiModelProperty("记录总条数")
    private long total;

    @ApiModelProperty("总页数")
    private int pages;

    @ApiModelProperty("数据列表")
    protected List<T> list;

}
