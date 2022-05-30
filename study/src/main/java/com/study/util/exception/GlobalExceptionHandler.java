package com.study.util.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 捕获全局异常统一处理器
 *
 * @author xudongmaster
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 通用异常处理
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler({Exception.class})
    public Result exceptionHandler(HttpServletRequest request, Exception exception) {
        exception.printStackTrace();
        return Result.error(exception.getMessage());
    }
}