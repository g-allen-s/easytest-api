package com.my.easytest.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "test_api_global_variable")
@Data
public class TestApiGlobalVariable extends BaseEntityNew {
    /**
     * 主键uuid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    @Column(name = "variable_key")
    private String variableKey;

    @Column(name = "variable_value")
    private String variableValue;

    /**
     * 模块id 为空表示全局，否则为模块内全局
     */
    @Column(name = "module_id")
    private Integer moduleId;

    public TestApiGlobalVariable () {
    }
    public TestApiGlobalVariable (Integer moduleId) {
        this.moduleId = moduleId;
    }
}