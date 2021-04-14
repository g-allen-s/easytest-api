package com.my.easytest.exception;

public abstract class AbstractRuntimeException extends RuntimeException {
    // private static final long serialVersionUID = 1L;

    public AbstractRuntimeException(String message) {
        super(message);
    }

    public AbstractRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public String getExceptionName() {
        return this.getClass().getSimpleName();
    }
}
