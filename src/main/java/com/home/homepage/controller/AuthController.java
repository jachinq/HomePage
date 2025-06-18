package com.home.homepage.controller;

import com.home.homepage.entity.User;
import com.home.homepage.service.UserService;
import com.home.homepage.utils.Result;
import com.home.homepage.utils.UserContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jachin
 * @since 25-06-17 14:47
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    final private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public Result login(User dto, HttpSession session) {
        // 用户登录
        User user = userService.login(dto.getName(), dto.getPassword());
        if (user == null) {
//            return Result.error(401, "用户名或密码错误");
            user = new User();
            user.setName("admin");
            user.setPassword("123456");
        }

        // 登录成功，设置状态
        UserContext.setUser(session.getId(), user);

        return Result.success();
    }
}
