package com.home.homepage.controller;

import com.home.homepage.service.CorsForwardService;
import com.home.homepage.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/cors")
public class CorsForwardController {
    private final CorsForwardService corsForwardService;

    @Autowired
    public CorsForwardController(CorsForwardService corsForwardService) {
        this.corsForwardService = corsForwardService;
    }

    @GetMapping("/bing")
    public Result getBing(@RequestParam String keyword) {
        return corsForwardService.getBing(keyword);
    }
}
