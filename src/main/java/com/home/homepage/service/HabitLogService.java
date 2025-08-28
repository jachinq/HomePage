package com.home.homepage.service;

import com.home.homepage.entity.HabitLog;
import com.home.homepage.entity.modal.BatchSaveHabitLog;
import com.home.homepage.entity.modal.HabitLogListModel;
import com.home.homepage.repository.HabitLogRepository;
import com.home.homepage.utils.Core;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        List<HabitLog> all = null;
        Integer habitId = model.getHabitId();
        if (habitId != null && habitId > 0) {
            all = habitLogRepository.findAllByUserIdAndHabitId(Core.getUid(), habitId);
        } else {
            all = habitLogRepository.findAllByUserId(Core.getUid());
        }
        return Result.success(all);
    }

    public Result del(HabitLog dto) {
        habitLogRepository.delete(dto);
        return Result.success(dto);
    }

    @Transactional
    public Result batchSave(BatchSaveHabitLog dto) {

        List<HabitLog> habitLogs = new ArrayList<>();
        List<Long> delList = dto.getDelList();
        if (delList != null && !delList.isEmpty()) {
            habitLogs.addAll(habitLogRepository.findAllById(delList));
            habitLogRepository.deleteAllById(delList);
        }

        List<HabitLog> setList = dto.getSetList();
        if (setList != null && !setList.isEmpty()) {
            habitLogs.addAll(habitLogRepository.saveAll(setList));
        }

        List<HabitLog> addList = dto.getAddList();
        if (addList != null && !addList.isEmpty()) {
            for (HabitLog habitLog : addList) {
                habitLog.setUserId(Core.getUid());
            }
            habitLogs.addAll(habitLogRepository.saveAll(addList));
        }

        return Result.success(habitLogs).setMessage("操作成功，一共修改了" + habitLogs.size() + "条数据");
    }
}
