package com.my.easytest.model.result;

import lombok.Data;

import java.util.List;

@Data
public class TestActionResult {

    private String testActionName;

    private String testResult;

    private String errMessage;

    private List<TestActionStepResult> testActionStepResults;

    public TestActionResult() {
        new TestActionResult("", "", "");
    }
    public TestActionResult(String testActionName, String testResult, String errMessage) {
        this.testActionName = testActionName;
        this.testResult = testResult;
        this.errMessage = errMessage;
    }

}
