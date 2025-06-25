package com.home.homepage.service;

import com.home.homepage.entity.HabitLog;
import com.home.homepage.entity.modal.HabitLogListModel;
import com.home.homepage.repository.HabitLogRepository;
import com.home.homepage.utils.Core;
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

    public Result save(HabitLog data) {
        data.setUserId(Core.getUid());
        HabitLog save = habitLogRepository.save(data);
        return Result.success(save);
    }

    public Result list(HabitLogListModel model) {
        List<HabitLog> all = habitLogRepository.findAllByUserIdAndHabitId(Core.getUid(), model.getHabitId());
        return Result.success(all);
    }

    public Result del(HabitLog dto) {
        habitLogRepository.delete(dto);
        return Result.success(dto);
    }
}
