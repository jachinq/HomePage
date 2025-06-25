package com.home.homepage.service;

import com.home.homepage.entity.Habit;
import com.home.homepage.repository.HabitRepository;
import com.home.homepage.utils.Core;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    public Result save(Habit data) {
        data.setUserId(Core.getUid());
        Habit save = habitRepository.save(data);
        return Result.success(save);
    }

    public Result list() {
        List<Habit> all = habitRepository.findAllByUserId(Core.getUid());
        return Result.success(all);
    }

    public Result del(Habit dto) {
        if (!Objects.equals(dto.getUserId(), Core.getUid())) {
            return Result.error(204,"不允许删除的习惯");
        }
        habitRepository.delete(dto);
        return Result.success(dto);
    }
}
