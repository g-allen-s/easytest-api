package com.my.easytest.exception;

import com.my.easytest.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常捕获类
 * 业务异常为使用ServiceException类包装的异常
 * @Author G_ALLEN
 * @create 2021-01-10
 **/
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    //业务异常
    @ExceptionHandler(ServiceException.class)
    public ResultDto serviceExceptionHandler(ServiceException ex) {
        return resultFormat(ex);
    }

    //其他异常
    @ExceptionHandler({Exception.class})
    public ResultDto exception(Exception ex) {
        return resultFormat(ex);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultDto exception(Throwable throwable) {
        log.error("服务暂不可用", throwable);
        return resultFormat(throwable);

    }

    private <T extends Throwable> ResultDto resultFormat(T ex) {
        log.error("全局异常捕获 == ", ex);
        ResultDto resultDto = ResultDto.newInstance();
        resultDto.setAsFailure();
        if(ex instanceof ServiceException){
            ServiceException serviceException = (ServiceException)ex;
            resultDto.setMessage(serviceException.getMessage());
        } else {
            resultDto.setMessage("服务暂不可用-" + ex.getMessage());
        }
        return resultDto;
    }

}