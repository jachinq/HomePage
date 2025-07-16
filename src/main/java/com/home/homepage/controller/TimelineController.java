package com.home.homepage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.homepage.entity.Timeline;
import com.home.homepage.service.TimelineService;
import com.home.homepage.utils.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 时间线控制器
 *
 * @author Jachin
 * @since 25-01-01 00:00
 */
@Slf4j
@RestController
@RequestMapping("/api/timeline")
public class TimelineController {
    
    private final TimelineService timelineService;
    
    @Autowired
    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }
    
    /**
     * 保存时间线事件
     */
    @PostMapping("/save")
    public Result save(@RequestBody Timeline timeline) {
        return timelineService.save(timeline);
    }
    
    /**
     * 获取时间线列表
     */
    @GetMapping("/list")
    public Result list() {
        return timelineService.list();
    }
    
    /**
     * 分页获取时间线列表
     */
    @GetMapping("/list/{page}/{size}")
    public Result list(@PathVariable int page, @PathVariable int size) {
        return timelineService.list(page, size);
    }
    
    /**
     * 根据ID获取时间线详情
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return timelineService.getById(id);
    }
    
    /**
     * 删除时间线事件
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return timelineService.delete(id);
    }
    
    /**
     * 根据日期范围查询时间线
     */
    @GetMapping("/date-range")
    public Result listByDateRange(@RequestParam String startDate, 
                                 @RequestParam String endDate) {
        return timelineService.listByDateRange(startDate, endDate);
    }
    
    /**
     * 根据分类查询时间线
     */
    @GetMapping("/category/{category}")
    public Result listByCategory(@PathVariable String category) {
        return timelineService.listByCategory(category);
    }
    
    /**
     * 根据优先级查询时间线
     */
    @GetMapping("/priority/{priority}")
    public Result listByPriority(@PathVariable String priority) {
        return timelineService.listByPriority(priority);
    }
    
    /**
     * 根据标题关键词搜索时间线
     */
    @GetMapping("/search")
    public Result search(@RequestParam String keyword) {
        return timelineService.search(keyword);
    }
    
    /**
     * 获取用户的所有分类
     */
    @GetMapping("/categories")
    public Result getCategories() {
        return timelineService.getCategories();
    }
    
    /**
     * 获取用户的时间线统计信息
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        return timelineService.getStatistics();
    }
} 