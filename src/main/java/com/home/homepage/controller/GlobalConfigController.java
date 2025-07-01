package com.home.homepage.controller;

import com.home.homepage.entity.AppConfig;
import com.home.homepage.entity.GlobalConfig;
import com.home.homepage.service.AppConfigService;
import com.home.homepage.service.GlobalConfigService;
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
@RequestMapping("/api/globalConfig")
public class GlobalConfigController {

    private final GlobalConfigService globalConfigService;

    @Autowired
    public GlobalConfigController(GlobalConfigService appSetService) {
        this.globalConfigService = appSetService;
    }

    @PostMapping("/save")
    public Result save(@RequestBody GlobalConfig dto){
        return globalConfigService.save(dto);
    }

    @GetMapping("/info")
    public Result info(){
        log.info("start get info");
        return globalConfigService.info();
    }

}
