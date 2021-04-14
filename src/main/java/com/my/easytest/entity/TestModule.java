package com.my.easytest.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "test_module")
@Data
public class TestModule extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 模块名
     */
    @Column(name = "module_name")
    private String moduleName;

    /**
     * 生产环境
     */
    @Column(name = "prd_host")
    private String prdHost;

    /**
     * 预生产环境
     */
    @Column(name = "pre_host")
    private String preHost;

    /**
     * 测试环境
     */
    @Column(name = "sit_host")
    private String sitHost;

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

    public TestModule () {
    }
    public TestModule (Integer moduleId) {
        this.id = moduleId;
    }

}