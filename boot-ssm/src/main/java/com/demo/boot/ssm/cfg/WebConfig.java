package com.demo.boot.ssm.cfg;

import com.demo.boot.ssm.web.interceptor.LoginInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.util.List;

@SpringBootConfiguration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());
        registration.excludePathPatterns("/login", "/register") // 注册、登录
                .excludePathPatterns("/token/**") // token 相关接口
                .excludePathPatterns("/test/**") // 前端测试
                .excludePathPatterns("/swagger-resources/**", "/webjars/**",
                        "/v2/**", "/swagger-ui.html/**")  // swagger-ui
                .addPathPatterns("/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(long2StringMessageConvert());
    }

    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将 long 转化为 string 的消息转化器，顺便处理了日期的格式
     *
     * @return 消息转化器
     */
    @Bean
    public MappingJackson2HttpMessageConverter long2StringMessageConvert() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();

        /*
         *  将Long,BigInteger序列化的时候,转化为String
         */
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);


        // 将转化模型设置到 ObjectMapper
        objectMapper.registerModule(simpleModule);

        // 设置时间格式
//        objectMapper.setDateFormat(new SimpleDateFormat(dateTimeFormat));

        // 为消息转化器设置 ObjectMapper
        messageConverter.setObjectMapper(objectMapper);

        // 返回消息转化器
        return messageConverter;
    }

}
