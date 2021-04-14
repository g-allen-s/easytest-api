package com.my.easytest.dto.api;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="API接口测试数据",description="API接口用例管理")
@Data
public class ApiTestDataPoolDto extends BaseEntityNew {

    /**
     * 数据池名称
     */
    @ApiModelProperty(value="数据池名称",required=true)
    private String datapoolName;

    /**
     * 数据池数据
     */
    @ApiModelProperty(value="数据池数据",required=true)
    private String datapoolParam;

    /**
     * 用例uid
     */
    @ApiModelProperty(value="用例uid",required=true)
    private Integer tcId;

}
