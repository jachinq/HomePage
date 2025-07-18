package com.home.homepage.entity;

import java.util.Calendar;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 时间线实体类
 *
 * @author Jachin
 * @since 25-01-01 00:00
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "timeline")
@Entity
public class Timeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private String eventDate; // 事件发生日期，格式：YYYY-MM-DD
    
    @Column(length = 20)
    private String eventTime; // 事件发生时间，格式：HH:mm
    
    @Column(length = 50)
    private String category; // 事件分类
    
    @Column(length = 20)
    private String priority; // 优先级：high, medium, low
    
    @Column(length = 500)
    private String tags; // 标签，以逗号分隔

    @Column(columnDefinition = "TEXT")
    private String attachments; // 附件路径，以逗号分隔
    
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isPublic = false; // 是否公开
    
    @CreationTimestamp
    private Calendar createTime;
    
    @UpdateTimestamp
    private Calendar updateTime;
} 