package com.home.homepage.controller;

import com.home.homepage.entity.User;
import com.home.homepage.entity.modal.ChangeUserPwd;
import com.home.homepage.service.UserService;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public Result info() {
        return userService.info();
    }

    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody ChangeUserPwd changeUserPwd) {
        return userService.changeUserPwd(changeUserPwd);
    }

    @PostMapping("/setUserAvatar")
    public Result setUserAvatar(@RequestBody User user) {
        return userService.setUserAvatar(user);
    }

}
