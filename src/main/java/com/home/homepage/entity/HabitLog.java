package com.home.homepage.entity;

import com.home.homepage.utils.CalendarUtil;
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
import org.hibernate.annotations.processing.Exclude;

import java.util.Calendar;

/**
 * 习惯表
 *
 * @author Jachin
 * @since 25-06-16 10:29
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "habit_log")
@Entity
public class HabitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long userId;
    private Integer habitId;
    private Boolean done;
    private Integer duration; // 持续时间，单位：s
    private String extra;
    private String date;

    @CreationTimestamp
    private Calendar createTime;
    @UpdateTimestamp
    private Calendar updateTime;
}
