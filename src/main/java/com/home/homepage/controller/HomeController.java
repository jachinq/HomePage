package com.home.homepage.controller;

import com.home.homepage.utils.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Jachin
 * @since 25-06-13 10:47
 */
@Controller
public class HomeController {

    @GetMapping(value = {"/", "/home"})
    public String home() {
        System.out.println("Welcome to Home Page");
        return "forward:/index.html";
    }

    @GetMapping("/test")
    @ResponseBody
    public Result test() {
        return Result.success("Hello, World!");
    }
}
