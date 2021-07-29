package com.demo.common;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

// 为了方便测试 mapper 提供的 Application 类，只配置 boot-smm 中的 mapper 层相关内容
@SpringBootApplication
@MapperScan(basePackages = {"com.demo.common.mapper", "com.demo.common.mapper2"})
//@ImportResource("classpath:trans.xml") // 使用声明式事务（不然 BaseServiceImpl 中的事务配置较麻烦）
//@EnableCaching
public class BootApplication {

}
