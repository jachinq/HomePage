package com.home.homepage.handle.exp;

import com.home.homepage.utils.Result;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.sqlite.SQLiteException;

import javax.naming.AuthenticationException;

/**
 * 全局异常处理器
 *
 * @author Jachin
 * @since 25-06-18 14:59
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpecificExceptionHandler {
    private String getPath(WebRequest webRequest) {
        return ((ServletWebRequest) webRequest).getRequest().getRequestURI();
    }

    // 处理认证异常
    @ExceptionHandler({AuthenticationException.class, JwtException.class})
    public Result handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        log.error("{}; {}", getPath(request), ex.getMessage(), ex);
        return Result.error(401, ex.getMessage());
    }

    // 处理数据库异常
    @ExceptionHandler(SQLiteException.class)
    public Result handleSQLiteException(SQLiteException ex, WebRequest request) {
        log.error("{}; {}", getPath(request), ex.getMessage(), ex);
        return Result.error(500, "数据库异常");
    }

}
