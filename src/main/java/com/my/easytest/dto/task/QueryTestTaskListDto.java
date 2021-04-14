package com.my.easytest.dto.task;

import com.my.easytest.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2020/6/15 11:42
 **/

@ApiModel(value="查询任务信息列表对象")
@Data
public class QueryTestTaskListDto extends BaseDto {

    @ApiModelProperty(value="Jenkins名称")
    private String taskName;

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;

}
