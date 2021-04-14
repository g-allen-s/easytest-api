package com.my.easytest.dto.api;

import com.my.easytest.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 11:42
 **/

@ApiModel(value="查询模块信息列表对象")
@Data
public class QueTestApiListDto extends BaseDto {

    @ApiModelProperty(value="接口编码")
    private String apiCode;

    @ApiModelProperty(value="接口名称")
    private String apiName;

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;

}
