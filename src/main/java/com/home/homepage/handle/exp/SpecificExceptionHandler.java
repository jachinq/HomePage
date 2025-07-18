package com.home.homepage.handle.exp;

import com.home.homepage.utils.Result;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.sqlite.SQLiteException;

import javax.naming.AuthenticationException;

/**
 * 全局异常处理器
 *
 * @author Jachin
 * @since 25-06-18 14:59
 */
@Slf4j
@ControllerAdvice
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

    // 请求方式异常
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        log.error("{}; {}", getPath(request), ex.getMessage(), ex);
        return Result.error(400, "请求方法错误" + ex.getMessage());
    }

    // 请求头类型异常
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result HttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        log.error("{}; {}", getPath(request), ex.getMessage(), ex);
        return Result.error(400, "请求类型不支持" + ex.getMessage());
    }

    // 从配置文件中获取生产环境域名
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxSize;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, WebRequest request) {
        log.error("{}; {}", getPath(request), ex.getMessage(), ex);
        return Result.error(400, "文件太大，超过" + maxSize);
    }
}
