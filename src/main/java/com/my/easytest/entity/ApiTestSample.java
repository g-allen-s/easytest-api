package com.my.easytest.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "test_api_test_action_samples")
@Data
public class ApiTestSample extends BaseEntityNew {
    /**
     * 主键uuid
     */
    @Id
    private String uid;

    @Column(name = "sample_source")
    private String sampleSource;

    @Column(name = "sample_target")
    private String sampleTarget;

    /**
     * 步骤uid
     */
    @Column(name = "test_action_id")
    private String testActionId;

    public ApiTestSample () {
    }
    public ApiTestSample (String testActionId) {
        this.testActionId = testActionId;
    }

 }