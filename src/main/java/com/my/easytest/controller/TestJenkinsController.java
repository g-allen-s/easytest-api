package com.my.easytest.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.easytest.common.TokenDb;
import com.my.easytest.constants.UserConstants;
import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.jenkins.AddTestJenkinsDto;
import com.my.easytest.dto.jenkins.QueryTestJenkinsListDto;
import com.my.easytest.dto.jenkins.UpdateTestJenkinsDto;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.entity.TestJenkins;
import com.my.easytest.service.TestJenkinsService;
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
 * @Date 2020/6/12 16:48
 **/
@Slf4j
@Api(tags = "Jenkins管理")
@RestController
@RequestMapping("/jenkins")
public class TestJenkinsController {

    @Autowired
    private TestJenkinsService testJenkinsService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param addTestJenkinsDto
     * @return
     */
    @ApiOperation(value = "添加Jenkins")
    @PostMapping
    public ResultDto<TestJenkins> save(HttpServletRequest request, @RequestBody AddTestJenkinsDto addTestJenkinsDto){

        log.info("添加Jenkins-入参= "+ JSONObject.toJSONString(addTestJenkinsDto));

        if(Objects.isNull(addTestJenkinsDto)){
            return ResultDto.success("Jenkins信息不能为空");
        }

        String name = addTestJenkinsDto.getJenkinsName();

        if(StringUtils.isEmpty(name)){
            return ResultDto.success("Jenkins名称不能为空");
        }

        TestJenkins testJenkins = new TestJenkins();
        CopyUtil.copyPropertiesCglib(addTestJenkinsDto, testJenkins);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        testJenkins.setCreateUserId(tokenDto.getUserId());

        String commandSuffix = addTestJenkinsDto.getCommandSuffix();
        //过滤待.的后缀，如.yml改为yml
        if(!StringUtils.isEmpty(commandSuffix)){
            testJenkins.setCommandSuffix(commandSuffix.replace(".",""));
        }

        ResultDto<TestJenkins> resultDto = testJenkinsService.save(tokenDto, testJenkins);
        return resultDto;
    }

    /**
     *
     * @param updateTestJenkinsDto
     * @return
     */
    @ApiOperation(value = "修改Jenkins")
    @PutMapping
    public ResultDto<TestJenkins> update(HttpServletRequest request, @RequestBody UpdateTestJenkinsDto updateTestJenkinsDto){

        log.info("修改Jenkins-入参= "+ JSONObject.toJSONString(updateTestJenkinsDto));

        if(Objects.isNull(updateTestJenkinsDto)){
            return ResultDto.success("Jenkins信息不能为空");
        }

        Integer jenkinsId = updateTestJenkinsDto.getId();
        String name = updateTestJenkinsDto.getJenkinsName();

        if(Objects.isNull(jenkinsId)){
            return ResultDto.success("JenkinsId不能为空");
        }


        if(StringUtils.isEmpty(name)){
            return ResultDto.success("Jenkins名称不能为空");
        }

        TestJenkins testJenkins = new TestJenkins();
        CopyUtil.copyPropertiesCglib(updateTestJenkinsDto, testJenkins);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        testJenkins.setCreateUserId(tokenDto.getUserId());

        String commandSuffix = updateTestJenkinsDto.getCommandSuffix();
        //过滤待.的后缀，如.yml改为yml
        if(!StringUtils.isEmpty(commandSuffix)){
            testJenkins.setCommandSuffix(commandSuffix.replace(".",""));
        }

        ResultDto<TestJenkins> resultDto = testJenkinsService.update(tokenDto, testJenkins);
        return resultDto;
    }

    /**
     *
     * @param jenkinsId
     * @return
     */
    @ApiOperation(value = "根据jenkinsId删除")
    @DeleteMapping("/{jenkinsId}")
    public ResultDto<TestJenkins> delete(HttpServletRequest request, @PathVariable Integer jenkinsId){

        log.info("根据jenkinsId删除-入参= "+ jenkinsId);

        if(Objects.isNull(jenkinsId)){
            return ResultDto.success("JenkinsId不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestJenkins> resultDto = testJenkinsService.delete(jenkinsId, tokenDto);
        return resultDto;
    }

    /**
     *
     * @param jenkinsId
     * @return
     */
    @ApiOperation(value = "根据jenkinsId查询")
    @GetMapping("/{jenkinsId}")
    public ResultDto<TestJenkins> getById(HttpServletRequest request, @PathVariable Integer jenkinsId){

        log.info("根据jenkinsId查询-入参= "+ jenkinsId);

        if(Objects.isNull(jenkinsId)){
            return ResultDto.success("JenkinsId不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestJenkins> resultDto = testJenkinsService.getById(jenkinsId, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param pageTableRequest
     * @return
     */
    @ApiOperation(value = "列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<TestJenkins>> list(HttpServletRequest request, PageTableRequest<QueryTestJenkinsListDto> pageTableRequest){

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        log.info("列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest) + "tokenDto=  " +JSONObject.toJSONString(tokenDto));

        QueryTestJenkinsListDto params = pageTableRequest.getParams();

        if(Objects.isNull(params)){
            params = new QueryTestJenkinsListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);

        ResultDto<PageTableResponse<TestJenkins>> responseResultDto = testJenkinsService.list(pageTableRequest);
        return responseResultDto;
    }

}
