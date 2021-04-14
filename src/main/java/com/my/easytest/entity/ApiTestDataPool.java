package com.my.easytest.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "test_api_test_data_pool")
@Data
public class ApiTestDataPool extends BaseEntityNew {
    /**
     * 主键uuid
     */
    @Id
    private String uid;

    @Column(name = "datapool_name")
    private String datapoolName;

    @Column(name = "datapool_param")
    private String datapoolParam;

    /**
     * 用例id
     */
    @Column(name = "tc_id")
    private Integer tcId;

    public ApiTestDataPool () {
    }
    public ApiTestDataPool (Integer tcId) {
        this.tcId = tcId;
    }
}