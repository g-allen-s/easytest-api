package com.my.easytest.dto.user;

import com.my.easytest.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="更新用户密码",description="用户对象user")
@Data
public class ChangePasswordDto extends BaseDto {

    @ApiModelProperty(value="用户名",required=true)
    private String userName;

    @ApiModelProperty(value="旧密码",required=true)
    private String oldPassword;

    @ApiModelProperty(value="新密码",required=true)
    private String newPassword;

}
