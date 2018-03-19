package com.boris.config;

import com.boris.common.exception.BorisException;
import com.boris.common.vo.ResponseVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BorisExceptionHandler {
    //controller层异常处理，service抛出的AdException，在controller不需要捕获直接抛出即可，在此处统一处理
    @org.springframework.web.bind.annotation.ExceptionHandler(BorisException.class)
    @ResponseBody
    public ResponseVo exceptionHandler(BorisException exception) {
        ResponseVo response = new ResponseVo();
        response.setCode(exception.getErrorCode());
        response.setMsg(exception.getMessage());
        response.setSuccess(false);
        response.setData(exception.getCause().getLocalizedMessage());
        return response;
    }
}
