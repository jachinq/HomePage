package com.home.homepage.controller;

import com.home.homepage.entity.AppConfig;
import com.home.homepage.entity.modal.ListModal;
import com.home.homepage.service.AppConfigService;
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
@RequestMapping("/api/appConfig")
public class AppConfigController {

    private final AppConfigService appConfigService;

    @Autowired
    public AppConfigController(AppConfigService appSetService) {
        this.appConfigService = appSetService;
    }

    @PostMapping("/add")
    public Result create(@RequestBody AppConfig dto){
        return appConfigService.save(dto);
    }

    @PostMapping("/update")
    public Result update(@RequestBody AppConfig dto){
        return appConfigService.update(dto);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody AppConfig dto){
        return appConfigService.delete(dto);
    }

    @GetMapping("/list")
    public Result list(ListModal dto){
        return appConfigService.list(dto);
    }

}
