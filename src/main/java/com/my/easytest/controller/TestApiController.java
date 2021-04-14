package com.my.easytest.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.easytest.common.TokenDb;
import com.my.easytest.constants.UserConstants;
import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.dto.api.*;
import com.my.easytest.entity.*;
import com.my.easytest.model.ApiGen;
import com.my.easytest.service.TestApiService;
import com.my.easytest.util.CopyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @Author G_ALLEN
 * @Date 2021/1/11 16:48
 **/
@Slf4j
@Api(tags = "API接口管理")
@RestController
@RequestMapping("/testApi")
public class TestApiController {

    @Autowired
    private TestApiService testApiService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param newTestApiDto
     * @return
     */
    @ApiOperation(value = "添加API接口")
    @PostMapping
    public ResultDto<TestApi> save(HttpServletRequest request, @RequestBody NewTestApiDto newTestApiDto){

        log.info("添加API接口-入参= "+ JSONObject.toJSONString(newTestApiDto));

        if(Objects.isNull(newTestApiDto)){
            return ResultDto.success("接口信息不能为空");
        }

        if(Objects.isNull(newTestApiDto.getApiCode())||Objects.isNull(newTestApiDto.getApiName())
                ||Objects.isNull(newTestApiDto.getApiMethod())||Objects.isNull(newTestApiDto.getApiModule())){
            return ResultDto.success("缺少参数");
        }

        String apiName = newTestApiDto.getApiName();
        if(StringUtils.isEmpty(apiName)){
            return ResultDto.success("接口名称不能为空");
        }

        String apiCode = newTestApiDto.getApiCode();
        if(StringUtils.isEmpty(apiCode)){
            return ResultDto.success("接口编码不能为空");
        }

        TestApi testApi = new TestApi();
        CopyUtil.copyPropertiesCglib(newTestApiDto, testApi);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        testApi.setCreateUserId(tokenDto.getUserId());

        ResultDto<TestApi> resultDto = testApiService.save(testApi);
        return resultDto;
    }

    /**
     *
     * @param modTestApiDto
     * @return
     */
    @ApiOperation(value = "更新API接口")
    @PutMapping
    public ResultDto<TestApi> update(HttpServletRequest request, @RequestBody ModTestApiDto modTestApiDto){

        log.info("更新API接口-入参= " + JSONObject.toJSONString(modTestApiDto));

        if(Objects.isNull(modTestApiDto)){
            return ResultDto.success("接口信息不能为空");
        }

        if(Objects.isNull(modTestApiDto.getId())){
            return ResultDto.success("缺少参数");
        }
        if(Objects.isNull(modTestApiDto.getApiCode())||Objects.isNull(modTestApiDto.getApiName())
                ||Objects.isNull(modTestApiDto.getApiMethod())||Objects.isNull(modTestApiDto.getApiModule())){
            return ResultDto.success("缺少参数");
        }

        String apiName = modTestApiDto.getApiName();
        if(StringUtils.isEmpty(apiName)){
            return ResultDto.success("接口名称不能为空");
        }

        String apiCode = modTestApiDto.getApiCode();
        if(StringUtils.isEmpty(apiCode)){
            return ResultDto.success("接口编码不能为空");
        }

        TestApi testApi = new TestApi();
        CopyUtil.copyPropertiesCglib(modTestApiDto, testApi);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        testApi.setCreateUserId(tokenDto.getUserId());

        ResultDto<TestApi> resultDto = testApiService.update(testApi);
        return resultDto;
    }

    /**
     *
     * @param apiId
     * @return
     */
    @ApiOperation(value = "根据ApiId删除")
    @DeleteMapping("/{apiId}")
    public ResultDto<TestApi> delete(HttpServletRequest request, @PathVariable Integer apiId){

        log.info("根据ApiId删除-入参= "+ apiId);

        if(Objects.isNull(apiId)){
            return ResultDto.success("ApiId不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestApi> resultDto = testApiService.delete(apiId, tokenDto);
        return resultDto;
    }

    /**
     *
     * @param pageTableRequest
     * @return
     */
    @ApiOperation(value = "接口列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<TestApi>> list(HttpServletRequest request, PageTableRequest<QueTestApiListDto> pageTableRequest){

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        log.info("列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest));

        QueTestApiListDto params = pageTableRequest.getParams();
        if(Objects.isNull(params)){
            params = new QueTestApiListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);

        ResultDto<PageTableResponse<TestApi>> responseResultDto = testApiService.list(pageTableRequest);
        return responseResultDto;
    }

    /**
     *
     * @param apiId
     * @return
     */
    @ApiOperation(value = "根据ApiId查询")
    @GetMapping("/{apiId}")
    public ResultDto<TestApi> getById(HttpServletRequest request, @PathVariable Integer apiId){

        log.info("根据ApiId查询-入参= "+ apiId);

        if(Objects.isNull(apiId)){
            return ResultDto.success("ApiId不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestApi> resultDto = testApiService.getById(apiId, tokenDto.getUserId());
        return resultDto;
    }


    /**
     *
     * @param testApiHeaderDto
     * @return
     */
    @ApiOperation(value = "添加API接口请求头")
    @PostMapping("/saveHeader")
    public ResultDto<TestApiHeader> saveHeader(HttpServletRequest request, @RequestBody TestApiHeaderDto testApiHeaderDto){

        log.info("添加API接口请求头-入参= "+ JSONObject.toJSONString(testApiHeaderDto));

        if(Objects.isNull(testApiHeaderDto)){
            return ResultDto.success("请求头信息不能为空");
        }

        if(Objects.isNull(testApiHeaderDto.getHeaderKey())||Objects.isNull(testApiHeaderDto.getHeaderValue())
                ||Objects.isNull(testApiHeaderDto.getHeaderType())||Objects.isNull(testApiHeaderDto.getApiId())){
            return ResultDto.success("缺少参数");
        }

        String apiName = testApiHeaderDto.getHeaderKey();
        if(StringUtils.isEmpty(apiName)){
            return ResultDto.success("请求头-键不能为空");
        }

        String apiCode = testApiHeaderDto.getHeaderValue();
        if(StringUtils.isEmpty(apiCode)){
            return ResultDto.success("请求头-值不能为空");
        }

        TestApiHeader header = new TestApiHeader();
        CopyUtil.copyPropertiesCglib(testApiHeaderDto, header);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestApiHeader> resultDto = testApiService.save(header, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "根据uid删除请求头")
    @DeleteMapping("/deleteHeader/{uid}")
    public ResultDto<TestApiHeader> deleteHeader(HttpServletRequest request, @PathVariable String uid){

        log.info("根据uid删除请求头-入参= "+ uid);

        if(Objects.isNull(uid)){
            return ResultDto.success("Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestApiHeader> resultDto = testApiService.deleteHeader(uid, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param testApiParamDto
     * @return
     */
    @ApiOperation(value = "添加API接口请求参数")
    @PostMapping("/saveParam")
    public ResultDto<TestApiParam> saveParam(HttpServletRequest request, @RequestBody TestApiParamDto testApiParamDto){

        log.info("添加API接口请求参数-入参= "+ JSONObject.toJSONString(testApiParamDto));

        if(Objects.isNull(testApiParamDto)){
            return ResultDto.success("请求参数信息不能为空");
        }

        if(Objects.isNull(testApiParamDto.getParamName())||Objects.isNull(testApiParamDto.getParamDesc())
                ||Objects.isNull(testApiParamDto.getParamType())||Objects.isNull(testApiParamDto.getApiId())){
            return ResultDto.success("缺少参数");
        }

        String apiName = testApiParamDto.getParamName();
        if(StringUtils.isEmpty(apiName)){
            return ResultDto.success("请求参数名称不能为空");
        }

        String apiCode = testApiParamDto.getParamDesc();
        if(StringUtils.isEmpty(apiCode)){
            return ResultDto.success("请求参数描述不能为空");
        }

        TestApiParam testApiParam = new TestApiParam();
        CopyUtil.copyPropertiesCglib(testApiParamDto, testApiParam);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestApiParam> resultDto = testApiService.save(testApiParam, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "根据uid删除请求参数")
    @DeleteMapping("/deleteParam/{uid}")
    public ResultDto<TestApiParam> deleteParam(HttpServletRequest request, @PathVariable String uid){

        log.info("根据uid删除请求参数-入参= "+ uid);

        if(Objects.isNull(uid)){
            return ResultDto.success("Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestApiParam> resultDto = testApiService.deleteParam(uid, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param testApiGlobalVariableDto
     * @return
     */
    @ApiOperation(value = "添加API接口全局变量")
    @PostMapping("/saveGlobal")
    public ResultDto<TestApiGlobalVariable> saveGlobal(HttpServletRequest request, @RequestBody TestApiGlobalVariableDto testApiGlobalVariableDto){

        log.info("添加API接口全局变量-入参= "+ JSONObject.toJSONString(testApiGlobalVariableDto));

        if(Objects.isNull(testApiGlobalVariableDto)){
            return ResultDto.success("全局变量信息不能为空");
        }

        if(Objects.isNull(testApiGlobalVariableDto.getVariableKey())
                ||Objects.isNull(testApiGlobalVariableDto.getVariableValue())){
            return ResultDto.success("缺少参数");
        }

        String apiName = testApiGlobalVariableDto.getVariableKey();
        if(StringUtils.isEmpty(apiName)){
            return ResultDto.success("变量-键不能为空");
        }

        String apiCode = testApiGlobalVariableDto.getVariableValue();
        if(StringUtils.isEmpty(apiCode)){
            return ResultDto.success("变量-值不能为空");
        }

        if(StringUtils.isEmpty(testApiGlobalVariableDto.getModuleId())){
            testApiGlobalVariableDto.setModuleId(0);
        }

        TestApiGlobalVariable globalVariable = new TestApiGlobalVariable();
        CopyUtil.copyPropertiesCglib(testApiGlobalVariableDto, globalVariable);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestApiGlobalVariable> resultDto = testApiService.save(globalVariable, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "根据uid删除全局变量")
    @DeleteMapping("/deleteGlobal/{uid}")
    public ResultDto<TestApiGlobalVariable> deleteGlobal(HttpServletRequest request, @PathVariable String uid){

        log.info("根据uid删除全局变量-入参= "+ uid);

        if(Objects.isNull(uid)){
            return ResultDto.success("Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestApiGlobalVariable> resultDto = testApiService.deleteGlobal(uid, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param newTestApiActionDto
     * @return
     */
    @ApiOperation(value = "添加API接口操作")
    @PostMapping("/saveAction")
    public ResultDto<TestApiAction> saveAction(HttpServletRequest request, @RequestBody NewTestApiActionDto newTestApiActionDto){

        log.info("添加API接口操作-入参= "+ JSONObject.toJSONString(newTestApiActionDto));

        if(Objects.isNull(newTestApiActionDto)){
            return ResultDto.success("操作信息不能为空");
        }

        if(Objects.isNull(newTestApiActionDto.getApiId())){
            return ResultDto.success("缺少参数");
        }

        if(StringUtils.isEmpty(newTestApiActionDto.getStepKeyword())){
            return ResultDto.success("关键字不能为空");
        }

        if(StringUtils.isEmpty(newTestApiActionDto.getStepKey())){
            return ResultDto.success("键不能为空");
        }

        if(StringUtils.isEmpty(newTestApiActionDto.getStepValue())){
            newTestApiActionDto.setStepValue("");
        }

        TestApiAction testApiAction = new TestApiAction();
        CopyUtil.copyPropertiesCglib(newTestApiActionDto, testApiAction);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestApiAction> resultDto = testApiService.save(testApiAction, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param
     * @return
     */
    @ApiOperation(value = "运行API接口action")
    @GetMapping("/run")
    public ResultDto run(HttpServletRequest request, RunTestApiActionDto runTestApiActionDto) {
        if(Objects.isNull(runTestApiActionDto)||Objects.isNull(runTestApiActionDto.getApiId())){
            return ResultDto.success("缺少参数");
        }

        if(StringUtils.isEmpty(runTestApiActionDto.getEnv())){
            return ResultDto.success("缺少参数");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto resultDto = testApiService.run(runTestApiActionDto.getApiId(), runTestApiActionDto.getEnv(), tokenDto.getUserId());
        return resultDto;
    }

    @ApiOperation(value = "导入接口")
    @PostMapping("file")
    public ResultDto importApi(HttpServletRequest request, @RequestParam("modelFile") MultipartFile modelFile, ImportTestApiDto importTestApiDto) {
        String fileName = importTestApiDto.getFileName();
        if (fileName.endsWith(".yaml")) {
            Yaml yaml = new Yaml();
            try {
                InputStream inputStream = modelFile.getInputStream();
                ApiGen apiGen = yaml.loadAs(inputStream, ApiGen.class);
                apiGen.setModuleId(importTestApiDto.getModuleId());
                inputStream.close();

                TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
                ResultDto resultDto = testApiService.generateTestApiKit(apiGen, tokenDto.getUserId());
                return resultDto;
            } catch (IOException e) {
                return ResultDto.fail("IOException::" + e.getMessage());
            }
        } else if (fileName.endsWith(".json")) {
            // TODO
            return ResultDto.fail("不支持的文件类型！");
        } else
            return ResultDto.fail("不支持的文件类型！");
    }
}
