package com.my.easytest.dto.task;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="添加任务对象",description="任务模块")
@Data
public class AddTestTaskDto extends BaseEntityNew {
    /**
     * 名称
     */
    @ApiModelProperty(value="任务名称",required=true)
    private String taskName;

    /**
     * 运行测试的Jenkins服务器id
     */
    @ApiModelProperty(value="运行测试的Jenkins服务器id",required=true)
    private Integer testJenkinsId;

    @ApiModelProperty(value="运行Job",required=true)
    private String jobName;

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;

}