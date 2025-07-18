package com.home.homepage.entity.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 文件列表查询模态框
 *
 * @author Jachin
 * @since 25-01-01 00:00
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FileListModal extends ListModal {

    /**
     * 文件类型过滤
     */
    private String fileType;

    /**
     * 文件名关键字搜索
     */
    private String keyword;

    /**
     * 文件大小范围 - 最小值（字节）
     */
    private Long minSize;

    /**
     * 文件大小范围 - 最大值（字节）
     */
    private Long maxSize;

    /**
     * 创建时间范围 - 开始时间
     */
    private String startDate;

    /**
     * 创建时间范围 - 结束时间
     */
    private String endDate;

    /**
     * 排序字段
     */
    private String sortBy = "createTime";

    /**
     * 排序方向
     */
    private String sortDirection = "desc";
} 