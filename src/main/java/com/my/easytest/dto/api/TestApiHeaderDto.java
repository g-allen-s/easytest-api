package com.my.easytest.dto.api;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="API接口请求头",description="API接口管理")
@Data
public class TestApiHeaderDto extends BaseEntityNew {

    /**
     * 请求头键
     */
    @ApiModelProperty(value="请求头键",required=true)
    private String headerKey;

    /**
     * 请求头值
     */
    @ApiModelProperty(value="请求头值",required=true)
    private String headerValue;

    /**
     * 请求头类型
     */
    @ApiModelProperty(value="请求头类型",required=true)
    private String headerType;

    /**
     * 关联接口ID
     */
    @ApiModelProperty(value="关联接口ID",required=true)
    private Integer apiId;

}
