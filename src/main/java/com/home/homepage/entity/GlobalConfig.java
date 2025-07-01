package com.home.homepage.entity;

import com.home.homepage.utils.CalendarUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "global_config")
@Entity
public class GlobalConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long userId;
    private String bgUrl;

    @CreationTimestamp
    @ToString.Exclude
    private Calendar createTime;
    @UpdateTimestamp
    @ToString.Exclude
    private Calendar updateTime;

    @ToString.Include
    private String creatTime() {
        return CalendarUtil.dateTimeFormat(createTime);
    }
    @ToString.Include
    private String updateTime() {
        return CalendarUtil.dateTimeFormat(updateTime);
    }
}
