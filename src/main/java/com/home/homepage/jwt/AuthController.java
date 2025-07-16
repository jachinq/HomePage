package com.home.homepage.jwt;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.homepage.entity.User;
import com.home.homepage.service.UserService;
import com.home.homepage.utils.Result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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
        String token = authResponse.getToken();
        try {
            String username = jwtUtil.extractUsername(token);
            User user = userService.loadUserByUsername(username);
            if (user != null && jwtUtil.validateToken(token, user)) {
                return Result.success();
            }
        } catch (Exception ignored) {
//            throw new Exception("token is invalid. token=" + token);
        }
        return Result.error(401, "token 失效，请重新登录");
    }

    // 获取token过期时间
    @PostMapping("/expiration")
    public Result getTokenExpiration(@RequestBody AuthResponse tokenRequest) {
        String token = tokenRequest.getToken();
        try {
            Date expiration = jwtUtil.extractExpiration(token);
            
            // 格式化日期
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedExpiration = dateFormat.format(expiration);
            
            return Result.success("获取token过期时间成功", formattedExpiration);
        } catch (Exception e) {
            log.error("获取token过期时间失败: ", e);
            return Result.error(400, "无效的token");
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
