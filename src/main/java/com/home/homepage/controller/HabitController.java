package com.home.homepage.controller;

import com.home.homepage.entity.AppConfig;
import com.home.homepage.entity.Habit;
import com.home.homepage.service.HabitService;
import com.home.homepage.utils.Core;
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
@RequestMapping("/api/habit")
public class HabitController {

    private final HabitService habitService;

    @Autowired
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping("/save")
    public Result save(@RequestBody Habit dto){
        return habitService.save(dto);
    }

    @PostMapping("/del")
    public Result delete(@RequestBody Habit dto){
        return habitService.del(dto);
    }

    @GetMapping("/list")
    public Result info(){
        return habitService.list();
    }

}
