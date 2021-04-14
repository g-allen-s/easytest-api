package com.my.easytest.dto.jenkins;

import com.my.easytest.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="更新Jenkins对象")
@Data
public class UpdateTestJenkinsDto extends BaseEntityNew {
    /**
     * 主键
     */
    @ApiModelProperty(value="Jenkins主键",required=true)
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(value="Jenkins名称",required=true)
    private String jenkinsName;

    /**
     * 测试命令
     */
    @ApiModelProperty(value="测试命令前缀",required=true)
    private String testCommand;

    /**
     * Jenkins的baseUrl
     */
    @ApiModelProperty(value="Jenkins的baseUrl",required=true)
    private String jenkinsUrl;

    /**
     * 用户名
     */
    @ApiModelProperty(value="Jenkins用户名称",required=true)
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value="Jenkins用户密码",required=true)
    private String password;

    /**
     *
     */
    @ApiModelProperty(value="是否设置为默认服务器 1 是 0 否",required=true)
    private Integer defaultFlag = 0;

    /**
     *
     */
    @ApiModelProperty(value="命令运行的测试用例类型  1 文本 2 文件",required=true)
    private Integer commandType = 1;

    /**
     *
     */
    @ApiModelProperty(value="测试用例后缀名 如果case为文件时，此处必填",required=true)
    private String commandSuffix;


}
