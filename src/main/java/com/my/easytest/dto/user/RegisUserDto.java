package com.my.easytest.dto.user;

import com.my.easytest.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="注册用户对象",description="用户对象user")
@Data
public class RegisUserDto extends BaseDto {

    @ApiModelProperty(value="用户名",required=true)
    private String userName;

    @ApiModelProperty(value="密码",required=true)
    private String password;

    @ApiModelProperty(value="邮箱",required=true)
    private String email;

}
