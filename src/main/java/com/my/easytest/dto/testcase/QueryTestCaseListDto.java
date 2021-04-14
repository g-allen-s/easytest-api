package com.my.easytest.dto.testcase;

import com.my.easytest.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2020/6/15 11:42
 **/

@ApiModel(value="查询测试用例信息列表对象")
@Data
public class QueryTestCaseListDto extends BaseDto {

    @ApiModelProperty(value="测试用例名称")
    private String caseName;

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;



}
