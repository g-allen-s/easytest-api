package com.my.easytest.model.result;

import lombok.Data;

@Data
public class TestActionStepResult {

    private String checkpoint;

    private String assertionX;

    private String expected;

    private String actual;

    private String result;

    private String errMsg;

}
