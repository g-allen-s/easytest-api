package com.my.easytest.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "test_api_header")
@Data
public class TestApiHeader extends BaseEntityNew {
    /**
     * 主键uuid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    @Column(name = "header_key")
    private String headerKey;

    @Column(name = "header_value")
    private String headerValue;

    /**
     * 类型：request_header response_header
     */
    @Column(name = "header_type")
    private String headerType;

    /**
     * 接口id
     */
    @Column(name = "api_id")
    private Integer apiId;

    public TestApiHeader () {
    }
    public TestApiHeader (Integer apiId, String headerType) {
        this.apiId = apiId;
        this.headerType = headerType;
    }
}