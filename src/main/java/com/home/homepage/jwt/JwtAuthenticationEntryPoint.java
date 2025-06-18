package com.home.homepage.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.homepage.utils.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * jwt认证EntryPoint, 用于处理未通过jwt认证的请求
 *
 * @author Jachin
 * @since 25-06-18 11:49
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Result result = Result.error(401, "Unauthorized");
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
