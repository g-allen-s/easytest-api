package com.my.easytest.exception;

import com.my.easytest.enums.Reason;

public class ParameterException extends AbstractRuntimeException {
    // private static final long serialVersionUID = -9186074002550846865L;

    public ParameterException(String message) {
        super(message);
    }

    public String getExceptionCnName() {
        return Reason.DATA.getReasonCnName();
    }

}
