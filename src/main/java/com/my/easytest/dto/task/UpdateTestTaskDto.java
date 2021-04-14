package com.my.easytest.dto.task;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="修改任务对象")
@Data
public class UpdateTestTaskDto extends BaseEntityNew {
    /**
     * ID
     */
    @ApiModelProperty(value="任务主键",required=true)
    private Integer id;

    /**
     * 任务名称
     */
    @ApiModelProperty(value="任务名称",required=true)
    private String taskName;

    /**
     * 任务名称
     */
    @ApiModelProperty(value="执行器",required=true)
    private Integer testJenkinsId;

    /**
     * Job名称
     */
    @ApiModelProperty(value="Job名称",required=true)
    private String jobName;

}