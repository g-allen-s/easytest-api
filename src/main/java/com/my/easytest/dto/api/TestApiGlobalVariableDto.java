package com.my.easytest.dto.api;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="API接口全局变量",description="API接口管理")
@Data
public class TestApiGlobalVariableDto extends BaseEntityNew {

    /**
     * 变量-键
     */
    @ApiModelProperty(value="变量-键",required=true)
    private String variableKey;

    /**
     * 变量-值
     */
    @ApiModelProperty(value="变量-值",required=true)
    private String variableValue;

    /**
     * 关联模块ID
     */
    @ApiModelProperty(value="关联模块ID",required=true)
    private Integer moduleId;

}
