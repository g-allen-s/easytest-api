package com.my.easytest.dto.api;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="API接口测试取样",description="API接口用例管理")
@Data
public class ApiTestSampleDto extends BaseEntityNew {

    /**
     * 取样来源
     */
    @ApiModelProperty(value="取样来源",required=true)
    private String sampleSource;

    /**
     * 取样目标
     */
    @ApiModelProperty(value="取样目标",required=true)
    private String sampleTarget;

    /**
     * 步骤uid
     */
    @ApiModelProperty(value="步骤uid",required=true)
    private String testActionId;

}
