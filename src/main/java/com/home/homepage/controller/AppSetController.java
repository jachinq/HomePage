package com.home.homepage.controller;

import com.home.homepage.entity.AppSet;
import com.home.homepage.entity.modal.AppSetListModal;
import com.home.homepage.service.AppSetService;
import com.home.homepage.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Jachin
 * @since 25-06-13 17:06
 */
@Slf4j
@RestController
@RequestMapping("/api/appSet")
public class AppSetController {

    private final AppSetService appSetService;

    @Autowired
    public AppSetController(AppSetService appSetService) {
        this.appSetService = appSetService;
    }

    @PostMapping("/save")
    public Result create(@RequestBody AppSet dto){
        return appSetService.save(dto);
    }

    @PostMapping("/batchSave")
    public Result batchSave(@RequestBody List<AppSet> dto){
        return appSetService.batchSave(dto);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody AppSet dto){
        return appSetService.delete(dto);
    }

    @GetMapping("/list")
    public Result list(AppSetListModal dto){
        return appSetService.list(dto);
    }

    @PostMapping("/import")
    public Result importData(@RequestBody List<AppSet> dto) {
        return appSetService.importData(dto);
    }
}
