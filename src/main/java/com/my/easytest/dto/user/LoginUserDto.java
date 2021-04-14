package com.my.easytest.dto.user;

import com.my.easytest.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author G_ALLEN
 * @Date 2021/1/02 16:49
 **/
@ApiModel(value="用户登录对象",description="用户对象user")
@Data
public class LoginUserDto extends BaseDto {

	@ApiModelProperty(value="用户名",name="username",required=true,dataType="String",notes="唯一不可重复",example="test01")
	private String userName;

	@ApiModelProperty(value="用户名",name="password",required=true,dataType="String",notes="字母+数字，6-18位",example="abc456")
	private String password;

}
