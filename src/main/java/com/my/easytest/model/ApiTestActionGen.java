package com.my.easytest.model;

import com.my.easytest.entity.ApiTestAssertion;
import com.my.easytest.entity.ApiTestSample;
import lombok.Data;

import java.util.List;

@Data
public class ApiTestActionGen {

    private String uid;

    private String testActionId;

    private String testActionName;

    private Integer tcId;

    private Integer apiId;

    private String apiCode;

    private List<ApiTestAssertion> testAssertions;

    private List<ApiTestSample> testSamples;

}
