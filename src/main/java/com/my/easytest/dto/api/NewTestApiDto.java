package com.my.easytest.dto.api;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="新增API接口",description="API接口管理")
@Data
public class NewTestApiDto extends BaseEntityNew {

    /**
     * 接口编码
     */
    @ApiModelProperty(value="接口编码",required=true)
    private String apiCode;

    /**
     * 接口名称
     */
    @ApiModelProperty(value="接口名称",required=true)
    private String apiName;

    /**
     * 请求方式
     */
    @ApiModelProperty(value="接口请求方式",required=true)
    private String apiMethod;

    /**
     * 模块ID
     */
    @ApiModelProperty(value="关联模块ID",required=true)
    private Integer apiModule;

}
