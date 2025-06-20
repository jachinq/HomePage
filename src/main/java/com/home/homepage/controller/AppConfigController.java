package com.home.homepage.controller;

import com.home.homepage.entity.AppConfig;
import com.home.homepage.entity.modal.ListModal;
import com.home.homepage.service.AppConfigService;
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
@RequestMapping("/api/appConfig")
public class AppConfigController {

    private final AppConfigService appConfigService;

    @Autowired
    public AppConfigController(AppConfigService appSetService) {
        this.appConfigService = appSetService;
    }

    @PostMapping("/save")
    public Result save(@RequestBody AppConfig dto){
        return appConfigService.save(dto);
    }

    @GetMapping("/info")
    public Result info(){
        log.info("start get info");
        return appConfigService.info();
    }

}
