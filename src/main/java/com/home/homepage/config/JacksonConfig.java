package com.home.homepage.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Jackson配置类
 *
 * @author Jachin
 * @since 25-06-13 18:07
 */
@Configuration
public class JacksonConfig {
    @Bean
    @Primary  // 覆盖默认实例
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
//                .registerModule(new Instan())  // 支持Java 8时间类型[7](@ref)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
//                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)  // 禁用时间戳格式
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)  // 忽略null字段[1,6](@ref)
//                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  // 忽略未知属性[2,5](@ref)
                ;
    }
}
