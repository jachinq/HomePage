package com.home.homepage.entity;

import com.home.homepage.utils.CalendarUtil;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * 应用配置
 *
 * @author Jachin
 * @since 25-06-16 10:29
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "app_config")
@Entity
public class AppConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long userId;
    private Boolean blankApp;       // 是否在新窗口打开应用
    private Boolean blankBookmark;  // 是否在新窗口打开书签
    private String innerDomain;     // 内网域名
    private String outerDomain;     // 外网域名

    @CreationTimestamp
    @ToString.Exclude
    private Calendar createTime;
    @UpdateTimestamp
    @ToString.Exclude
    private Calendar updateTime;

    @ToString.Include
    private String creatTime() {
        return CalendarUtil.format(createTime);
    }
    @ToString.Include
    private String updateTime() {
        return CalendarUtil.format(updateTime);
    }
}
