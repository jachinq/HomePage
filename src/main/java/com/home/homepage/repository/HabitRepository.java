package com.home.homepage.repository;

import com.home.homepage.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jachin
 * @since 25-06-13 17:21
 */
@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findAllByUserId(Long uid);
}
