package com.home.homepage.controller;

import com.home.homepage.entity.SexDaily;
import com.home.homepage.service.SexDailyService;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jachin
 * @since 25-06-13 17:06
 */
@RestController
@RequestMapping("/sex-daily")
public class SexDailyController {

    @Autowired
    private SexDailyService sexService;

    @PostMapping("/add")
    public Result create(@RequestBody SexDaily dto){
        sexService.save(dto);
        return Result.success("ok");
    }

    @GetMapping("/list")
    public Result list(){
        return sexService.list();
    }

}
