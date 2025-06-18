package com.home.homepage.handle.exp;

import com.home.homepage.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * 全局异常处理器
 *
 * @author Jachin
 * @since 25-06-18 14:59
 */
@Slf4j
@RestControllerAdvice
@Order() // 使用默认的优先级：低
public class GenericExceptionHandler {
    private String getPath(WebRequest webRequest) {
        return ((ServletWebRequest) webRequest).getRequest().getRequestURI();
    }

    // 处理所有未知异常
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex, WebRequest request) {
        log.error("{}; {}", getPath(request), ex.getMessage(), ex);
        return Result.error(500, "系统异常");
    }
}
