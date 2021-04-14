package com.my.easytest.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "test_api_param")
@Data
public class TestApiParam extends BaseEntityNew {
    /**
     * 主键uuid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    @Column(name = "param_name")
    private String paramName;

    @Column(name = "param_desc")
    private String paramDesc;

    /**
     * 类型：request_param response_param
     */
    @Column(name = "param_type")
    private String paramType;

    /**
     * 是否必填： 1 必填 0 非必填
     */
    @Column(name = "is_required")
    private Byte isRequired;

    /**
     * 接口id
     */
    @Column(name = "api_id")
    private Integer apiId;

    public TestApiParam () {
    }
    public TestApiParam (Integer apiId, String paramType) {
        this.apiId = apiId;
        this.paramType = paramType;
    }
}