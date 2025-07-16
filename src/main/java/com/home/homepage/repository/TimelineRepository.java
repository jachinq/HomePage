package com.home.homepage.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.home.homepage.entity.Timeline;

/**
 * 时间线数据访问层
 *
 * @author Jachin
 * @since 25-01-01 00:00
 */
@Repository
public interface TimelineRepository extends JpaRepository<Timeline, Long> {
    
    /**
     * 根据用户ID查询时间线列表
     */
    List<Timeline> findAllByUserIdOrderByEventDateDesc(Long userId);
    
    /**
     * 分页查询用户的时间线
     */
    Page<Timeline> findAllByUserIdOrderByEventDateDesc(Long userId, Pageable pageable);
    
    /**
     * 根据用户ID和日期范围查询时间线
     */
    @Query("SELECT t FROM Timeline t WHERE t.userId = :userId AND t.eventDate BETWEEN :startDate AND :endDate ORDER BY t.eventDate DESC")
    List<Timeline> findByUserIdAndDateRange(@Param("userId") Long userId, 
                                          @Param("startDate") String startDate, 
                                          @Param("endDate") String endDate);
    
    /**
     * 根据用户ID和分类查询时间线
     */
    List<Timeline> findAllByUserIdAndCategoryOrderByEventDateDesc(Long userId, String category);
    
    /**
     * 根据用户ID和优先级查询时间线
     */
    List<Timeline> findAllByUserIdAndPriorityOrderByEventDateDesc(Long userId, String priority);
    
    /**
     * 根据用户ID和标题关键词查询时间线
     */
    @Query("SELECT t FROM Timeline t WHERE t.userId = :userId AND t.title LIKE %:keyword% ORDER BY t.eventDate DESC")
    List<Timeline> findByUserIdAndTitleKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);
    
    /**
     * 查询用户的所有分类
     */
    @Query("SELECT DISTINCT t.category FROM Timeline t WHERE t.userId = :userId AND t.category IS NOT NULL")
    List<String> findCategoriesByUserId(@Param("userId") Long userId);
    
    /**
     * 统计用户的时间线数量
     */
    long countByUserId(Long userId);
} 