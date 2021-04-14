package com.my.easytest.model;

import com.my.easytest.entity.TestApiAction;
import com.my.easytest.entity.TestApiHeader;
import com.my.easytest.entity.TestApiParam;
import lombok.Data;

import java.util.List;

@Data
public class Api {

    private Integer apiId;

    private String apiCode;

    private String apiName;

    private String apiMethod;

    private String apiStatus;

    private Integer apiModule;

    private List<TestApiHeader> apiHeaders;

    private List<TestApiParam> requestParams;

    private List<TestApiParam> responseParams;

    private List<TestApiAction> apiActions;

}
