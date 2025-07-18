package com.home.homepage.entity.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 文件上传模态框
 *
 * @author Jachin
 * @since 25-01-01 00:00
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FileUploadModal {

    /**
     * 文件描述
     */
    private String description;

    /**
     * 文件分类
     */
    private String category;

    /**
     * 是否公开
     */
    private Boolean isPublic = false;

    /**
     * 标签
     */
    private String tags;
} 