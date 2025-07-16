package com.home.homepage.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.home.homepage.entity.Timeline;
import com.home.homepage.repository.TimelineRepository;
import com.home.homepage.utils.Core;
import com.home.homepage.utils.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * 时间线服务类
 *
 * @author Jachin
 * @since 25-01-01 00:00
 */
@Slf4j
@Service
public class TimelineService {
    
    private final TimelineRepository timelineRepository;
    
    @Autowired
    public TimelineService(TimelineRepository timelineRepository) {
        this.timelineRepository = timelineRepository;
    }
    
    /**
     * 保存时间线事件
     */
    public Result save(Timeline timeline) {
        try {
            // 设置当前用户ID
            timeline.setUserId(Core.getUid());
            
            // 参数验证
            if (!StringUtils.hasText(timeline.getTitle())) {
                return Result.error(400, "事件标题不能为空");
            }
            if (!StringUtils.hasText(timeline.getEventDate())) {
                return Result.error(400, "事件日期不能为空");
            }
            
            // 设置默认值
            if (timeline.getPriority() == null) {
                timeline.setPriority("medium");
            }
            if (timeline.getIsPublic() == null) {
                timeline.setIsPublic(false);
            }
            
            Timeline saved = timelineRepository.save(timeline);
            log.info("用户 {} 保存了时间线事件: {}", Core.getUid(), saved.getTitle());
            return Result.success(saved);
        } catch (Exception e) {
            log.error("保存时间线事件失败", e);
            return Result.error(500, "保存失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取时间线列表
     */
    public Result list() {
        try {
            Long userId = Core.getUid();
            List<Timeline> timelines = timelineRepository.findAllByUserIdOrderByEventDateDesc(userId);
            return Result.success(timelines);
        } catch (Exception e) {
            log.error("获取时间线列表失败", e);
            return Result.error(500, "获取列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页获取时间线列表
     */
    public Result list(int page, int size) {
        try {
            Long userId = Core.getUid();
            Pageable pageable = PageRequest.of(page, size);
            Page<Timeline> timelines = timelineRepository.findAllByUserIdOrderByEventDateDesc(userId, pageable);
            return Result.success(timelines);
        } catch (Exception e) {
            log.error("分页获取时间线列表失败", e);
            return Result.error(500, "获取列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取时间线详情
     */
    public Result getById(Long id) {
        try {
            Optional<Timeline> timelineOpt = timelineRepository.findById(id);
            if (!timelineOpt.isPresent()) {
                return Result.error(404, "时间线事件不存在");
            }
            
            Timeline timeline = timelineOpt.get();
            if (!Objects.equals(timeline.getUserId(), Core.getUid())) {
                return Result.error(403, "无权限访问该时间线事件");
            }
            
            return Result.success(timeline);
        } catch (Exception e) {
            log.error("获取时间线详情失败", e);
            return Result.error(500, "获取详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除时间线事件
     */
    public Result delete(Long id) {
        try {
            Optional<Timeline> timelineOpt = timelineRepository.findById(id);
            if (!timelineOpt.isPresent()) {
                return Result.error(404, "时间线事件不存在");
            }
            
            Timeline timeline = timelineOpt.get();
            if (!Objects.equals(timeline.getUserId(), Core.getUid())) {
                return Result.error(403, "无权限删除该时间线事件");
            }
            
            timelineRepository.delete(timeline);
            log.info("用户 {} 删除了时间线事件: {}", Core.getUid(), timeline.getTitle());
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除时间线事件失败", e);
            return Result.error(500, "删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据日期范围查询时间线
     */
    public Result listByDateRange(String startDate, String endDate) {
        try {
            Long userId = Core.getUid();
            List<Timeline> timelines = timelineRepository.findByUserIdAndDateRange(userId, startDate, endDate);
            return Result.success(timelines);
        } catch (Exception e) {
            log.error("根据日期范围查询时间线失败", e);
            return Result.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据分类查询时间线
     */
    public Result listByCategory(String category) {
        try {
            Long userId = Core.getUid();
            List<Timeline> timelines = timelineRepository.findAllByUserIdAndCategoryOrderByEventDateDesc(userId, category);
            return Result.success(timelines);
        } catch (Exception e) {
            log.error("根据分类查询时间线失败", e);
            return Result.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据优先级查询时间线
     */
    public Result listByPriority(String priority) {
        try {
            Long userId = Core.getUid();
            List<Timeline> timelines = timelineRepository.findAllByUserIdAndPriorityOrderByEventDateDesc(userId, priority);
            return Result.success(timelines);
        } catch (Exception e) {
            log.error("根据优先级查询时间线失败", e);
            return Result.error(500, "查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据标题关键词搜索时间线
     */
    public Result search(String keyword) {
        try {
            Long userId = Core.getUid();
            List<Timeline> timelines = timelineRepository.findByUserIdAndTitleKeyword(userId, keyword);
            return Result.success(timelines);
        } catch (Exception e) {
            log.error("搜索时间线失败", e);
            return Result.error(500, "搜索失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户的所有分类
     */
    public Result getCategories() {
        try {
            Long userId = Core.getUid();
            List<String> categories = timelineRepository.findCategoriesByUserId(userId);
            return Result.success(categories);
        } catch (Exception e) {
            log.error("获取分类列表失败", e);
            return Result.error(500, "获取分类失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户的时间线统计信息
     */
    public Result getStatistics() {
        try {
            Long userId = Core.getUid();
            long totalCount = timelineRepository.countByUserId(userId);
            List<String> categories = timelineRepository.findCategoriesByUserId(userId);
            
            return Result.success(new Object() {
                public final long total = totalCount;
                public final int categoryCount = categories.size();
                public final List<String> categoryList = categories;
            });
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return Result.error(500, "获取统计失败: " + e.getMessage());
        }
    }
} 