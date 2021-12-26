package com.hao.bundle.demo.pojo.dto;

import io.swagger.annotations.ApiModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@ToString
@ApiModel(description = "通用测试类")
public class Foo {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Integer age;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Float height;

    @Getter
    @Setter
    private Double weight;

    @Getter
    @Setter
    private Date date;

    @Getter
    @Setter
    private LocalDateTime localDateTime;

    @Getter
    @Setter
    private LocalDate localDate;

    @Getter
    @Setter
    private LocalTime localTime;

    private String noGetter = "noGetter";

    private final String finalNoGetter = "finalNoGetter";

    @Getter
    private final String finalGetter = "finalGetter";

    public static String staticTime = "2021-12-25";

}
