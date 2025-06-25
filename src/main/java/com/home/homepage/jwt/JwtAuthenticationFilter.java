package com.home.homepage.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.homepage.entity.User;
import com.home.homepage.service.UserService;
import com.home.homepage.utils.Core;
import com.home.homepage.utils.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 用于处理每个请求的JWT令牌验证
 * 继承自OncePerRequestFilter确保每个请求只被过滤一次
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * 构造器注入所需的依赖
     *
     * @param jwtUtil            JWT工具类，用于处理JWT令牌
     * @param userDetailsService 用户详情服务，用于加载用户信息
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userService = userDetailsService;
    }

    /**
     * 处理每个请求的JWT认证
     * 1. 从请求头中提取JWT令牌
     * 2. 验证令牌的有效性
     * 3. 如果令牌有效，设置认证信息到SecurityContext
     *
     * @param request      HTTP请求
     * @param response     HTTP响应
     * @param filterChain  过滤器链
     * @throws IOException      IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            // 放行 OPTIONS 请求
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }

            // 从请求头中获取Authorization
            final String authHeader = request.getHeader("Authorization");
            
            // 如果请求头中没有Bearer令牌，直接放行
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 提取JWT令牌（去掉"Bearer "前缀）
            final String token = authHeader.substring(7);
            // 从令牌中提取用户名
            final String username = jwtUtil.extractUsername(token);

            // 如果用户名存在且当前没有认证信息
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 加载用户详情
                User userDetails = userService.loadUserByUsername(username);
                
                // 验证令牌
                if (userDetails != null && jwtUtil.validateToken(token, userDetails)) {
                    // 创建认证令牌
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    // 设置认证详情
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 将认证信息设置到SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    Core.setUser(userDetails);
                } else {
                    sendErrorResponse(response, 401, "Invalid token", "Token is invalid or expired");
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorResponse(response, 401, e.getMessage(), "token is invalid or expired");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status,
                                   String error, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(status);
        Result result = Result.error(status, message + ": " + error);
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
} 