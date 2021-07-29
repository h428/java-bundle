package com.demo.boot.ssm;

import com.demo.base.component.util.SnowflakeIdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.demo.base.component.mapper", "com.demo.base.component.mapper2"})
//@ImportResource("classpath:trans.xml") // 使用声明式事务（不然 BaseServiceImpl 中的事务配置较麻烦）
@EnableCaching
//@EnableTransactionManagement // 可以不用配置
public class BootSsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootSsmApplication.class, args);
    }

//    @Bean
//    public DataSource dataSource(JdbcProperties jdbcProperties) {
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setUrl(jdbcProperties.getUrl());
//        dataSource.setDriverClassName(jdbcProperties.getDriverClassName());
//        dataSource.setUsername(jdbcProperties.getUsername());
//        dataSource.setPassword(jdbcProperties.getPassword());
//        return dataSource;
//    }

    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        return new SnowflakeIdWorker(0L, 0L);
    }

}
