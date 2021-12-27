package com.hao.bundle.demo.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
@MapperScan("com.hao.bundle.demo.mapper")
public class MybatisPlusConfig {

}
