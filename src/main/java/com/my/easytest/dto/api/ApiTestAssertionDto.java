package com.my.easytest.dto.api;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="API接口测试断言",description="API接口用例管理")
@Data
public class ApiTestAssertionDto extends BaseEntityNew {

    /**
     * 断言-键
     */
    @ApiModelProperty(value="断言-键",required=true)
    private String assertionKey;

    /**
     * 断言-值
     */
    @ApiModelProperty(value="断言-值",required=true)
    private String assertionValue;

    /**
     * 断言类型
     */
    @ApiModelProperty(value="断言类型",required=true)
    private String assertionType;

    /**
     * 步骤uid
     */
    @ApiModelProperty(value="步骤uid",required=true)
    private String testActionId;

}
