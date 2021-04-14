package com.my.easytest.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.easytest.common.Token;
import com.my.easytest.common.TokenDb;
import com.my.easytest.constants.UserConstants;
import com.my.easytest.dto.user.RegisUserDto;
import com.my.easytest.dto.user.LoginUserDto;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.dto.user.ChangePasswordDto;
import com.my.easytest.entity.TestUser;
import com.my.easytest.service.TestUserService;
import com.my.easytest.util.CopyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author G_ALLEN
 * @Date 2021/1/11 16:48
 **/
@Slf4j
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class TestUserController {

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param regisUserDto
     * @return
     */
    @ApiOperation(value = "用户注册", notes="")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultDto<TestUser> save(@RequestBody RegisUserDto regisUserDto){

        log.info("用户注册-入参= " + regisUserDto);

        if(Objects.isNull(regisUserDto)){
            return ResultDto.success("用户信息不能为空");
        }

        String userName = regisUserDto.getUserName();

        if(StringUtils.isEmpty(userName)){
            return ResultDto.success("用户名不能为空");
        }

        String pwd = regisUserDto.getPassword();

        if(StringUtils.isEmpty(pwd)){
            return ResultDto.success("密码不能为空");
        }

        TestUser testUser = new TestUser();
        CopyUtil.copyPropertiesCglib(regisUserDto, testUser);
        ResultDto<TestUser> resultDto = testUserService.save(testUser);
        return resultDto;
    }

    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public ResultDto<Token> login(@RequestBody LoginUserDto loginUserDto) {

        log.info("用户登录-入参= " + JSONObject.toJSONString(loginUserDto));

        String userName = loginUserDto.getUserName();
        String password = loginUserDto.getPassword();
        log.info("username= " + userName);
        if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(password)){
            return ResultDto.fail("用户名或密码不能为空");
        }

        ResultDto<Token> resultDto = testUserService.login(userName, password);

        return resultDto;
    }

    @ApiOperation(value = "登出接口")
    @DeleteMapping("/logout")
    public ResultDto logout(HttpServletRequest request) {

        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        boolean loginFlag = tokenDb.isLogin(token);

        if(!loginFlag){
            return ResultDto.fail("用户未登录，无需退出");
        }

        TokenDto tokenDto = tokenDb.removeTokenDto(token);

        return ResultDto.success("成功", tokenDto);
    }

    @ApiOperation(value = "是否已经登录接口")
    @GetMapping("/isLogin")
    public ResultDto<TokenDto> isLogin(HttpServletRequest request) {

        String token = request.getHeader(UserConstants.LOGIN_TOKEN);

        boolean loginFlag = tokenDb.isLogin(token);

        TokenDto tokenDto = tokenDb.getTokenDto(token);

        return ResultDto.success("成功", tokenDto);
    }

    /**
     *
     * @param changePasswordDto
     * @return
     */
    @ApiOperation(value = "更新密码")
    @PutMapping
    public ResultDto<TestUser> changePassword(@RequestBody ChangePasswordDto changePasswordDto){

        log.info("更新密码-入参= " + changePasswordDto);

        if(Objects.isNull(changePasswordDto)){
            return ResultDto.success("用户更新密码信息不能为空");
        }

        String userName = changePasswordDto.getUserName();
        if(StringUtils.isEmpty(userName)){
            return ResultDto.success("用户名不能为空");
        }

        String oldPassword = changePasswordDto.getOldPassword();
        if(StringUtils.isEmpty(oldPassword)){
            return ResultDto.success("旧密码不能为空");
        }

        String newPassword = changePasswordDto.getNewPassword();
        if(StringUtils.isEmpty(newPassword)){
            return ResultDto.success("新密码不能为空");
        }

        ResultDto<TestUser> resultDto = testUserService.changePassword(userName, oldPassword, newPassword);
        return resultDto;
    }

}
