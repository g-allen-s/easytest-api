package com.my.easytest.model.result;

import lombok.Data;

import java.util.List;

@Data
public class TestDDTResult {

    private String ddtName;

    private String testResult;

    private String errMessage;

    private List<TestActionResult> testActionResultList;

}
