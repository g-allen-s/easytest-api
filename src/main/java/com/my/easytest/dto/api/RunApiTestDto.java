package com.my.easytest.dto.api;

import com.my.easytest.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="运行API接口测试",description="API接口用例管理")
@Data
public class RunApiTestDto extends BaseDto {

    /**
     * 用例ID
     */
    @ApiModelProperty(value="用例ID",required=true)
    private Integer tcId;

    /**
     * 运行环境
     */
    @ApiModelProperty(value="运行环境",required=true)
    private String env;

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
