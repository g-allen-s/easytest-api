package com.my.easytest.dto.module;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="新增测试模块",description="测试模块")
@Data
public class NewTestModuleDto extends BaseEntityNew {

    @ApiModelProperty(value="模块名",required=true,dataType="String",example="商品模块")
     private String moduleName;

    @ApiModelProperty(value="生产环境",required=false,dataType="String",example="")
    private String prdHost;

    @ApiModelProperty(value="预生产环境",required=false,dataType="String",example="")
    private String preHost;

    @ApiModelProperty(value="测试环境",required=false,dataType="String",example="")
    private String sitHost;

}
