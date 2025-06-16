package com.home.homepage.service;

import com.home.homepage.entity.Habit;
import com.home.homepage.repository.HabitRepository;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jachin
 * @since 25-06-13 17:23
 */
@Service
public class HabitService {
    private final HabitRepository habitRepository;

    @Autowired
    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public void save(Habit data) {
        Habit save = habitRepository.save(data);
        System.out.println(save);
    }

    public Result list() {
        List<Habit> all = habitRepository.findAll();
        return Result.success(all, all.size());
    }
}
