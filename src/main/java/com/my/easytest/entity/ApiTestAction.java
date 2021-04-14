package com.my.easytest.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "test_api_test_action")
@Data
public class ApiTestAction extends BaseEntityNew {
    /**
     * 主键uuid
     */
    @Id
    private String uid;

    /**
     * 测试用例步骤
     */
    @Column(name = "test_action_name")
    private String testActionName;

    /**
     * 用例id
     */
    @Column(name = "tc_id")
    private Integer tcId;

    /**
     * 接口id
     */
    @Column(name = "api_id")
    private Integer apiId;

    /**
     * 创建人id
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    public ApiTestAction () {
    }
    public ApiTestAction (Integer tcId) {
        this.tcId = tcId;
    }
}