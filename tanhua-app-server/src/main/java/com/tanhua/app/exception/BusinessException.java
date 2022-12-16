package com.tanhua.app.exception;

import com.tanhua.commons.enums.ErrorResult;
import lombok.Data;

/**
 * @description: 业务异常
 * @author: 16420
 * @time: 2022/12/16 17:40
 */

@Data
public class BusinessException extends RuntimeException{

    private ErrorResult errorResult;

    public BusinessException(ErrorResult errorResult){
        super(errorResult.getErrMessage());
        this.errorResult = errorResult;
    }

}
