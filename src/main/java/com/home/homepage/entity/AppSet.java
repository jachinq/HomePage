package com.home.homepage.entity;

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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Calendar;

/**
 * 应用集
 *
 * @author Jachin
 * @since 25-06-16 10:29
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "appSet")
@Entity
public class AppSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long userId;
    private Integer type; // 类型 0-系统 1-自定义
    private Integer status; // 状态 0-禁用 1-启用
    private Integer port; // 端口号
    private String name;
    private String description;
    private String cover; // 封面
    private String icon; // 图标
    private String background; // 背景图
    private String tags; // 标签
    private String category; // 分类
    private String innerUrl; // 内网链接
    private String outerUrl; // 外网链接

    @CreationTimestamp
    private Calendar createTime;
    @UpdateTimestamp
    private Calendar updateTime;
}
