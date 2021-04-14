package com.my.easytest.exception;

/**
 * 服务异常类
 * @author G_ALLEN
 * @description
 * @create 2021/1/10 18:02
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServiceException(final String message, Throwable th){
        super(message,th);
        this.message = message;
    }

    public ServiceException(final String message){
        this.message = message;
    }

    public static void throwEx(String message){
        throw new ServiceException(message);
    }

}
