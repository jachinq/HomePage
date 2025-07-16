package com.home.homepage.entity.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 时间线查询参数
 *
 * @author Jachin
 * @since 25-01-01 00:00
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TimelineQuery {
    
    private String keyword;        // 关键词搜索
    private String category;       // 分类过滤
    private String priority;       // 优先级过滤
    private String startDate;      // 开始日期
    private String endDate;        // 结束日期
    private Boolean isPublic;      // 是否公开
    private Integer page;          // 页码
    private Integer size;          // 每页大小
    private String sortBy;         // 排序字段
    private String sortDirection;  // 排序方向：ASC, DESC
} 