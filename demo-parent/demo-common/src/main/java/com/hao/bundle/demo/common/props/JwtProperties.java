package com.hao.bundle.demo.common.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@EnableConfigurationProperties(JwtProperties.class)
@Data
public class JwtProperties {
    private String pubKeyPath;// 公钥地址
    private String priKeyPath;// 私钥地址
}
