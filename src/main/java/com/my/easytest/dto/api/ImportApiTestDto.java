package com.my.easytest.dto.api;

import com.my.easytest.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="导入接口测试",description="API接口用例管理")
@Data
public class ImportApiTestDto extends BaseDto {

    /**
     * 模块ID
     */
    @ApiModelProperty(value="模块ID",required=true)
    private Integer moduleId;

    /**
     * 文件名称
     */
    @ApiModelProperty(value="文件名称",required=true)
    private String fileName;

}
