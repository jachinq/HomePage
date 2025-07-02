package com.home.homepage.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

/**
* Cors跨域配置
 *
* @author Jachin
* @since 25-06-18 14:04
*/
@Component
public class CorsConfig implements CorsConfigurationSource {

    // 从配置文件中获取生产环境域名
    @Value("${homepage.domain.pro}")
    private String domainPro;

    @Override

    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration configuration = new CorsConfiguration();

        // 配置允许的源（生产环境应指定具体域名而非"*"）
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000", domainPro
        ));
        // 配置允许的方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 配置允许的请求头
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // 是否允许发送凭据（如cookies）
        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L); // 预检请求的缓存时间（秒）
        return configuration;
    }
}
