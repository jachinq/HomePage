package com.home.homepage.controller;

import com.home.homepage.entity.AppSet;
import com.home.homepage.entity.modal.AppSetListModal;
import com.home.homepage.service.AppSetService;
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
@RequestMapping("/api/appSet")
public class AppSetController {

    private final AppSetService appSetService;

    @Autowired
    public AppSetController(AppSetService appSetService) {
        this.appSetService = appSetService;
    }

    @PostMapping("/add")
    public Result create(@RequestBody AppSet dto){
        return appSetService.save(dto);
    }

    @PostMapping("/update")
    public Result update(@RequestBody AppSet dto){
        return appSetService.update(dto);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody AppSet dto){
        return appSetService.delete(dto);
    }

    @GetMapping("/list")
    public Result list(AppSetListModal dto){
        return appSetService.list(dto);
    }

}
