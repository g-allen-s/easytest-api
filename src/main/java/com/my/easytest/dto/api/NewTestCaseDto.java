package com.my.easytest.dto.api;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="新增API接口用例",description="API接口用例管理")
@Data
public class NewTestCaseDto extends BaseEntityNew {

    /**
     * 用例编码
     */
    @ApiModelProperty(value="用例编码",required=true)
    private String apiTcCode;

    /**
     * 用例名称
     */
    @ApiModelProperty(value="用例名称",required=true)
    private String apiTcName;

    /**
     * 用例等级
     */
    @ApiModelProperty(value="用例等级",required=true)
    private String apiTcPriority;

    /**
     * 模块ID
     */
    @ApiModelProperty(value="关联模块ID",required=true)
    private Integer apiModule;

}
