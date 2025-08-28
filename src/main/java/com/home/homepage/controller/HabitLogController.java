package com.home.homepage.controller;

import com.home.homepage.entity.Habit;
import com.home.homepage.entity.HabitLog;
import com.home.homepage.entity.modal.BatchSaveHabitLog;
import com.home.homepage.entity.modal.HabitLogListModel;
import com.home.homepage.service.HabitLogService;
import com.home.homepage.service.HabitService;
import com.home.homepage.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jachin
 * @since 25-06-13 17:06
 */
@Slf4j
@RestController
@RequestMapping("/api/habitLog")
public class HabitLogController {

    private final HabitLogService habitLogService;

    @Autowired
    public HabitLogController(HabitLogService habitLogService) {
        this.habitLogService = habitLogService;
    }

    @PostMapping("/save")
    public Result save(@RequestBody HabitLog dto){
        return habitLogService.save(dto);
    }

    @PostMapping("/batchSave")
    public Result batchSave(@RequestBody BatchSaveHabitLog dto){
        return habitLogService.batchSave(dto);
    }

    @PostMapping("/del")
    public Result delete(@RequestBody HabitLog dto){
        return habitLogService.del(dto);
    }

    @GetMapping("/list")
    public Result list(HabitLogListModel model){
        return habitLogService.list(model);
    }

}
