package com.my.easytest.dto.api;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="新增API接口操作",description="API接口管理")
@Data
public class NewTestApiActionDto extends BaseEntityNew {

    /**
     * 接口ID
     */
    @ApiModelProperty(value="接口ID",required=true)
    private Integer apiId;

    /**
     * 操作关键字
     */
    @ApiModelProperty(value="操作关键字",required=true)
    private String stepKeyword;

    /**
     * 操作-键
     */
    @ApiModelProperty(value="操作-键",required=true)
    private String stepKey;

    /**
     * 操作-值
     */
    @ApiModelProperty(value="操作-值",required=true)
    private String stepValue;

}
