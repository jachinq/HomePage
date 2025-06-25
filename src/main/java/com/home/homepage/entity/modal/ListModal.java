package com.home.homepage.entity.modal;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 分页查询参数封装类
 *
 * @author Jachin
 * @since 25-06-17 10:02
 */
@Data
public class ListModal {
    private Integer pageNum = 1;        // 当前页数
    private Integer pageSize = 10;      // 每页显示的数量
    private String sort;          // 排序字段
    private Boolean desc = true;    // 排序方式

    public Pageable getPageable() {
        Integer pageSize = getPageSize();
        if (pageSize == null || pageSize <= 0) {
            return null;
        }
        Integer pageNum = getPageNum();
        String sort = getSort();
        boolean desc = getDesc();
        Sort.Direction sortDirection = Sort.Direction.ASC;
        if (desc) {
            sortDirection = Sort.Direction.DESC;
        }
        if (sort == null) {
            return PageRequest.of(pageNum - 1, pageSize);
        }
        return PageRequest.of(pageNum - 1, pageSize, Sort.by(sortDirection, sort));
    }
}
