package com.my.easytest.dto.api;

import com.my.easytest.dto.BaseDto;
import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="运行API接口action",description="API接口管理")
@Data
public class RunTestApiActionDto extends BaseDto {

    /**
     * 接口ID
     */
    @ApiModelProperty(value="接口ID",required=true)
    private Integer apiId;

    /**
     * 运行环境
     */
    @ApiModelProperty(value="运行环境",required=true)
    private String env;

}
