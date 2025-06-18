package com.home.homepage.jwt;

import com.home.homepage.config.CorsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置类
 * 用于配置应用程序的安全策略、认证方式和授权规则
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * 构造器注入所需的依赖
     *
     * @param jwtAuthFilter      JWT认证过滤器
     * @param userDetailsService 用户详情服务
     * @param passwordEncoder    密码编码器
     */
    public SecurityConfig(
            CorsConfig corsConfig, JwtAuthenticationFilter jwtAuthFilter,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.corsConfig = corsConfig;
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    /**
     * 配置安全过滤器链
     * 定义请求的访问规则、认证方式和会话管理策略
     *
     * @param http HttpSecurity对象
     * @return SecurityFilterChain
     * @throws Exception 配置过程中的异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护，因为我们使用JWT进行认证
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用默认的跨域配置
                .cors(c -> c.configurationSource(corsConfig))
                // 配置请求授权规则
                .authorizeHttpRequests(auth -> {
                    // 允许所有用户访问认证相关的端点
                    auth.requestMatchers("/api/auth/**").permitAll();
                    // 所有 /api 开头的请求都需要验证
                    auth.requestMatchers("/api/**").authenticated();
                    // 其他所有请求都不需要验证
                    auth.anyRequest().permitAll();
                })
                // 配置会话管理
                .sessionManagement(session -> session
                        // 使用无状态会话，因为我们使用JWT
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 配置认证提供者
                .authenticationProvider(authenticationProvider())
                // 添加JWT过滤器
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // 配置未授权时的异常处理
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        return http.build();
    }

    /**
     * 配置认证提供者
     * 设置用户详情服务和密码编码器
     *
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 设置用户详情服务，用于加载用户信息
        authProvider.setUserDetailsService(userDetailsService);
        // 设置密码编码器，用于密码验证
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * 配置认证管理器
     * 用于处理认证请求
     *
     * @param config AuthenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception 配置过程中的异常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
} 