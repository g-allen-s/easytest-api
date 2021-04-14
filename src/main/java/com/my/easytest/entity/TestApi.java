package com.my.easytest.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "test_api")
@Data
public class TestApi extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 接口码
     */
    @Column(name = "api_code")
    private String apiCode;

    /**
     * 接口名称
     */
    @Column(name = "api_name")
    private String apiName;

    /**
     * 请求方式
     */
    @Column(name = "api_method")
    private String apiMethod;

    /**
     * 状态 0 无效 1 启用
     */
    @Column(name = "api_status")
    private Byte apiStatus;

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

    public TestApi () {
    }
    public TestApi (Integer apiId) {
        this.id = apiId;
    }
    public TestApi (String apiCode) {
        this.apiCode = apiCode;
    }

}