package com.my.easytest.model.result;

import lombok.Data;

import java.util.List;

@Data
public class TestResult {

    private String testName; // test case jenkinsName

    private String testResult;

    private String errMessage;

    private List<TestDDTResult> testDDTResult;

    public TestResult() {
        new TestResult("", "", "");
    }
    public TestResult(String testName, String testResult, String errMessage) {
        this.testName = testName;
        this.testResult = testResult;
        this.errMessage = errMessage;
    }

}
