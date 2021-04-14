package com.my.easytest.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "test_api_test_case")
@Data
public class ApiTestCase extends BaseEntityNew {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用例编码
     */
    @Column(name = "api_tc_code")
    private String apiTcCode;

    /**
     * 用例名称
     */
    @Column(name = "api_tc_name")
    private String apiTcName;

    /**
     * 用例等级：L0,L1,L2
     */
    @Column(name = "api_tc_priority")
    private String apiTcPriority;

    /**
     * 状态：0-无效 1-启用
     */
    @Column(name = "api_tc_status")
    private Byte apiTcStatus;

    /**
     * 模块id
     */
    @Column(name = "api_module")
    private Integer apiModule;

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

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    public ApiTestCase () {
    }
    public ApiTestCase (Integer tcId) {
        this.id = tcId;
    }
}