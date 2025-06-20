package com.home.homepage.jwt;

import com.home.homepage.entity.User;
import com.home.homepage.service.UserService;
import com.home.homepage.utils.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jachin
 * @since 25-06-17 14:47
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserService userService,
            JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public Result register(@RequestBody User request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        // 检查用户名是否存在
        if (userService.loadUserByUsername(request.getUsername()) != null) {
            return Result.error(400, "用户名已存在");
        }

        User save = userService.save(user);
        return Result.success("注册成功", save);
    }

    @PostMapping("/login")
    public Result login(@RequestBody User request) {
        System.out.println("auth start");
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            log.error("e: ", e);
            return Result.error(401, "用户名或密码错误");
        }

        System.out.println("login start");
        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);
        log.info("token: {}, user: {}", token, user);
        return Result.success(new AuthResponse(token, user));
    }

    // 验证token有效性
    @PostMapping("/validate")
    @SneakyThrows
    public Result validate(@RequestBody AuthResponse authResponse) {
        Thread.sleep(3 * 1000); // 模拟延迟
        String token = authResponse.getToken();
        String username = jwtUtil.extractUsername(token);
        User user = userService.loadUserByUsername(username);
        if (user != null && jwtUtil.validateToken(token, user)) {
            return Result.success(user);
        }else {
            return Result.error(401, "token 失效，请重新登录");
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private User user;
    }
}
