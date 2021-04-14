package com.my.easytest.dto.api;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="API前置测试",description="API接口用例管理")
@Data
public class PreTestDto extends BaseEntityNew {

    /**
     * 前置测试类型
     */
    @ApiModelProperty(value="前置测试类型",required=true)
    private String preTestMethod;

    /**
     * 前置测试对象
     */
    @ApiModelProperty(value="前置测试对象",required=true)
    private String preTestTarget;

    /**
     * 前置请求参数
     */
    @ApiModelProperty(value="前置请求参数",required=true)
    private String requestParam;

    /**
     * 前置取样参数
     */
    @ApiModelProperty(value="前置取样参数",required=true)
    private String samplesParam;

    /**
     * 关联用例ID
     */
    @ApiModelProperty(value="关联用例ID",required=true)
    private Integer tcId;

}
