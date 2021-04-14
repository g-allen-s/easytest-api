package com.my.easytest.exception;

import com.my.easytest.enums.Reason;

public class FileNotFoundException extends AbstractRuntimeException {
    // private static final long serialVersionUID = -8369246115234949967L;

    public FileNotFoundException(String psMessage) {
        super("Read file error : " + psMessage);
    }

    public FileNotFoundException(String psMessage, Throwable cause) {
        super("Read file error : " + psMessage, cause);
    }

    public String getExceptionCnName() {
        return Reason.DATA.getReasonCnName();
    }
}
