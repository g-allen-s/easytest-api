package com.my.easytest.model;

import com.my.easytest.entity.ApiPreTest;
import com.my.easytest.entity.ApiTestDataPool;
import lombok.Data;

import java.util.List;

@Data
public class ApiTest {

    private Integer apiTcId;

    private String apiTcCode;

    private String apiTcName;

    private String apiTcPriority;

    private Byte apiTcStatus;

    private Integer apiModule;

    private List<ApiPreTest> preTests;

    private List<ApiTestDataPool> testDataPool;

    private List<ApiTestActionGen> testActions;

}
