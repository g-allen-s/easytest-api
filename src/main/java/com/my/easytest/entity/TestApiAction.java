package com.my.easytest.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "test_api_action")
@Data
public class TestApiAction extends BaseEntityNew {
    /**
     * 主键uuid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    /**
     * 接口id
     */
    @Column(name = "api_id")
    private Integer apiId;

    /**
     * 步骤id
     */
    @Column(name = "step_id")
    private Byte stepId;

    /**
     * 关键字
     */
    @Column(name = "step_keyword")
    private String stepKeyword;

    @Column(name = "step_key")
    private String stepKey;

    @Column(name = "step_value")
    private String stepValue;

    public TestApiAction () {
    }
    public TestApiAction (Integer apiId) {
        this.apiId = apiId;
    }

}