package com.my.easytest.dto.api;

import com.my.easytest.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="运行多个API接口测试",description="API接口用例管理")
@Data
public class RunMultipleApiTestDto extends BaseDto {

    /**
     * 用例ID
     */
    @ApiModelProperty(value="用例ID集合",required=true)
    private List<Integer> tcIds;

    /**
     * 运行环境
     */
    @ApiModelProperty(value="运行环境",required=true)
    private String env;

    /**
     * 并行方式
     */
    @ApiModelProperty(value="并行方式",required=true)
    private String runParallel;

    /**
     * 延迟时间
     */
    @ApiModelProperty(value="延迟时间",required=false)
    String delayValue;

    /**
     * 延迟单位
     */
    @ApiModelProperty(value="延迟单位",required=false)
    String delayUnit;

    /**
     * 计划时间
     */
    @ApiModelProperty(value="计划时间",required=false)
    String scheduledTime;

}
