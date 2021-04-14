package com.my.easytest.exception;

import com.my.easytest.enums.Reason;

public class ClassNotFoundException extends AbstractRuntimeException {
    private static final long serialVersionUID = 1L;

    public ClassNotFoundException(String message) {
        super(message);
    }

    public ClassNotFoundException(String message, Throwable e) {
        super(message, e);
    }

    public String getExceptionCnName() {
        return Reason.TESTRUNNER.getReasonCnName();
    }
}

