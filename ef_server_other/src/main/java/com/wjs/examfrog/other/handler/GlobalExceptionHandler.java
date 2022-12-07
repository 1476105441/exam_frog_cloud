package com.wjs.examfrog.other.handler;

import com.wjs.examfrog.common.CommonResult;
import com.wjs.examfrog.common.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理对象
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Result<String>> captureException(Exception e){
        e.printStackTrace();
        return CommonResult.failed(510,e.getMessage());
    }
}
