package com.my.easytest.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "test_jenkins")
@Data
public class TestJenkins extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Jenkins名称
     */
    @Column(name = "jenkins_name")
    private String jenkinsName;

    /**
     * Jenkins服务器baseUrl
     */
    @Column(name = "jenkins_url")
    private String jenkinsUrl;

    /**
     * Jenkins用户名
     */
    private String username;

    /**
     * Jenkins密码
     */
    private String password;

    /**
     * 命令运行的测试用例类型  1 文本 2 文件
     */
    @Column(name = "command_type")
    private Integer commandType;

    /**
     * 测试用例后缀名 如果case为文件时，此处必填
     */
    @Column(name = "command_suffix")
    private String commandSuffix;

    /**
     * 测试命令
     */
    @Column(name = "test_command")
    private String testCommand;

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

    private Integer defaultFlag;

}