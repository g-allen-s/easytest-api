package com.my.easytest.dto.api;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="API接口请求参数",description="API接口管理")
@Data
public class TestApiParamDto extends BaseEntityNew {

    /**
     * 参数名称
     */
    @ApiModelProperty(value="参数名称",required=true)
    private String paramName;

    /**
     * 参数描述
     */
    @ApiModelProperty(value="参数描述",required=true)
    private String paramDesc;

    /**
     * 参数类型
     */
    @ApiModelProperty(value="参数类型",required=true)
    private String paramType;

    /**
     * 是否必填
     */
    @ApiModelProperty(value="是否必填",required=true)
    private Byte isRequired;

    /**
     * 关联接口ID
     */
    @ApiModelProperty(value="关联接口ID",required=true)
    private Integer apiId;

}
