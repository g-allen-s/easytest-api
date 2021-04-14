package com.my.easytest.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "test_api_pre_test")
@Data
public class ApiPreTest extends BaseEntityNew {
    /**
     * 主键uuid
     */
    @Id
    private String uid;

    /**
     * 类型：api,sql
     */
    @Column(name = "pre_test_method")
    private String preTestMethod;

    /**
     * 前置测试对象
     */
    @Column(name = "pre_test_target")
    private String preTestTarget;

    /**
     * 前置请求参数
     */
    @Column(name = "request_param")
    private String requestParam;

    /**
     * 前置取样参数
     */
    @Column(name = "samples_Param")
    private String samplesParam;

    /**
     * 用例id
     */
    @Column(name = "tc_id")
    private Integer tcId;

    public ApiPreTest () {
    }
    public ApiPreTest (Integer tcId) {
        this.tcId = tcId;
    }

}