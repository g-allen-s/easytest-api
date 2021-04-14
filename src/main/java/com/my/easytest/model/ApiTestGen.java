package com.my.easytest.model;

import com.my.easytest.entity.TestApiGlobalVariable;
import lombok.Data;

import java.util.List;

@Data
public class ApiTestGen {

    private Integer moduleId;

    private List<TestApiGlobalVariable> globalParams;

    private List<ApiTest> apiTestCases;

}
