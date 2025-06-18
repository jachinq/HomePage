package com.home.homepage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 请求配置
 *
 * @author Jachin
 * @since 25-06-17 17:25
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String home = "forward:/index.html";
        // 处理根路径
        registry.addViewController("/").setViewName(home);
        // 处理所有不包含.的路径（排除静态资源）, 如：http://localhost:8080/login
        registry.addViewController("/{x:[\\w\\-]+}").setViewName(home);
    }
}
