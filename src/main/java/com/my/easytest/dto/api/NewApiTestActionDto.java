package com.my.easytest.dto.api;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="新增API接口用例步骤",description="API接口用例管理")
@Data
public class NewApiTestActionDto extends BaseEntityNew {

    /**
     * 用例步骤
     */
    @ApiModelProperty(value="用例步骤",required=true)
    private String testActionName;

    /**
     * 用例ID
     */
    @ApiModelProperty(value="用例ID",required=true)
    private Integer tcId;

    /**
     * 接口ID
     */
    @ApiModelProperty(value="接口ID",required=true)
    private Integer apiId;

}
