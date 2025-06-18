package com.home.homepage.service;

import com.home.homepage.entity.HabitLog;
import com.home.homepage.repository.HabitLogRepository;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jachin
 * @since 25-06-13 17:23
 */
@Service
public class HabitLogService {
    private final HabitLogRepository habitLogRepository;

    @Autowired
    public HabitLogService(HabitLogRepository habitLogRepository) {
        this.habitLogRepository = habitLogRepository;
    }

    public void save(HabitLog data) {
        HabitLog save = habitLogRepository.save(data);
        System.out.println(save);
    }

    public Result list() {
        List<HabitLog> all = habitLogRepository.findAll();
        return Result.success(all);
    }
}
