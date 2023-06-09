package com.example.web_test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //进行跨域配置
        //前端占用8080 后端占用8888
        //两个端口之间的访问就是跨域
        //要允许8080端口访问8888
        registry.addMapping("/**").allowedOrigins("http://192.168.10.51:8080/");
        registry.addMapping("/**").allowedOrigins("http://192.168.43.83:8080/");
    }
}
