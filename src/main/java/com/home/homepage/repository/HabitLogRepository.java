package com.home.homepage.repository;

import com.home.homepage.entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jachin
 * @since 25-06-13 17:21
 */
@Repository
public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
    List<HabitLog> findAllByUserIdAndHabitId(Long uid, Integer habitId);

    List<HabitLog> findAllByUserId(Long uid);
}
