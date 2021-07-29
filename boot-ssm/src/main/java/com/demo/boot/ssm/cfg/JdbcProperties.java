package com.demo.boot.ssm.cfg;

import lombok.Data;

//@ConfigurationProperties(prefix = "jdbc")
@Data
public class JdbcProperties {
    private String url;
    private String driverClassName;
    private String username;
    private String password;
}
