package com.my.easytest.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "test_api_test_action_assertion")
@Data
public class ApiTestAssertion extends BaseEntityNew {
    /**
     * 主键uuid
     */
    @Id
    private String uid;

    @Column(name = "assertion_type")
    private String assertionType;

    @Column(name = "assertion_key")
    private String assertionKey;

    @Column(name = "assertion_value")
    private String assertionValue;

    /**
     * 步骤uid
     */
    @Column(name = "test_action_id")
    private String testActionId;

    public ApiTestAssertion () {
    }
    public ApiTestAssertion (String testActionId) {
        this.testActionId = testActionId;
    }
}