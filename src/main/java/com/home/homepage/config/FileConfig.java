package com.home.homepage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件服务配置类
 *
 * @author Jachin
 * @since 25-01-01 00:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileConfig {

    /**
     * 文件上传路径
     */
    private String path;

    /**
     * 单个文件最大大小
     */
    private String maxSize;

    /**
     * 请求最大大小
     */
    private String maxRequestSize;

    /**
     * 允许的文件类型
     */
    private List<String> allowedTypes;

    /**
     * 获取文件上传路径，确保以/结尾
     */
    public String getUploadPath() {
        if (path != null && !path.endsWith("/")) {
            return path + "/";
        }
        return path;
    }

    /**
     * 检查文件类型是否被允许
     */
    public boolean isAllowedFileType(String fileType) {
        if (allowedTypes == null || allowedTypes.isEmpty()) {
            return true;
        }
        return allowedTypes.contains(fileType.toLowerCase());
    }
} 