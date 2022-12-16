package com.tanhua.app.exception;

import com.tanhua.commons.enums.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @description: 全局异常处理器
 * @author: 16420
 * @time: 2022/12/16 18:11
 */
@ControllerAdvice
public class ExceptionAdvice {

    //业务异常
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity handlerException(BusinessException be){
        be.printStackTrace();
        ErrorResult errorResult = be.getErrorResult();
        return ResponseEntity.status(Integer.valueOf(errorResult.getErrCode())).body(errorResult.getErrMessage());
    }

    //其他异常
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handlerException1(Exception e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
