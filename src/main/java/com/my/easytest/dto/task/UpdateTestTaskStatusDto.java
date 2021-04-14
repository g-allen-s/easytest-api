package com.my.easytest.dto.task;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="修改任务状态对象")
@Data
public class UpdateTestTaskStatusDto extends BaseEntityNew {

    /**
     * ID
     */
    @ApiModelProperty(value="任务主键",required=true)
    private Integer id;

    /**
     *
     */
    @ApiModelProperty(value="构建地址",required=true)
    private String buildUrl;

    /**
     *
     */
    @ApiModelProperty(value="任务状态码",required=true)
    private Integer status;

}