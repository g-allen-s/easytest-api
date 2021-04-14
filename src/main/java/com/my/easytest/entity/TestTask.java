package com.my.easytest.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "test_task")
@Data
public class TestTask extends BaseEntityNew {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 任务名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 运行测试的Jenkins服务器
     */
    @Column(name = "test_jenkins_id")
    private Integer testJenkinsId;

    /**
     * Jenkins执行测试的Job名称
     */
    @Column(name = "job_name")
    private String jobName;

    /**
     * Jenkins执行测试的构建url
     */
    @Column(name = "build_url")
    private String buildUrl;

    /**
     * 用例数量
     */
    @Column(name = "case_count")
    private Integer caseCount;

    /**
     * 状态 0 无效 1 新建 2 执行中 3 执行完成
     */
    private Integer status;

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

    /**
     * Jenkins执行测试的命令脚本
     */
    @Column(name = "test_command")
    private String testCommand;

//    public TestTask () {
//    }
//    public TestTask (Integer testTaskId) {
//        this.id = testTaskId;
//    }

}