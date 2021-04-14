package com.my.easytest.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.easytest.common.TokenDb;
import com.my.easytest.constants.UserConstants;
import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.dto.module.ModTestModuleDto;
import com.my.easytest.dto.module.NewTestModuleDto;
import com.my.easytest.dto.module.QueTestModuleListDto;
import com.my.easytest.entity.TestModule;
import com.my.easytest.service.TestModuleService;
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
@Api(tags = "模块管理")
@RestController
@RequestMapping("/module")
public class TestModuleController {

    @Autowired
    private TestModuleService testModuleService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param newTestModuleDto
     * @return
     */
    @ApiOperation(value = "添加模块")
    @PostMapping
    public ResultDto<TestModule> save(@RequestBody NewTestModuleDto newTestModuleDto){

        log.info("添加模块-入参= "+ JSONObject.toJSONString(newTestModuleDto));

        if(Objects.isNull(newTestModuleDto)){
            return ResultDto.success("模块信息不能为空");
        }

        if(Objects.isNull(newTestModuleDto.getPrdHost())||Objects.isNull(newTestModuleDto.getPreHost())
                ||Objects.isNull(newTestModuleDto.getSitHost())||Objects.isNull(newTestModuleDto.getModuleName())){
            return ResultDto.success("缺少参数");
        }

        String name = newTestModuleDto.getModuleName();
        if(StringUtils.isEmpty(name)){
            return ResultDto.success("模块名称不能为空");
        }

        TestModule testModule = new TestModule();
        CopyUtil.copyPropertiesCglib(newTestModuleDto, testModule);

        ResultDto<TestModule> resultDto = testModuleService.save(testModule);
        return resultDto;
    }

    /**
     *
     * @param modTestModuleDto
     * @return
     */
    @ApiOperation(value = "更新模块")
    @PutMapping
    public ResultDto<TestModule> update(@RequestBody ModTestModuleDto modTestModuleDto){

        log.info("更新模块信息-入参= " + JSONObject.toJSONString(modTestModuleDto));

        if(Objects.isNull(modTestModuleDto)){
            return ResultDto.success("模块信息不能为空");
        }

        if(Objects.isNull(modTestModuleDto.getModuleName())||Objects.isNull(modTestModuleDto.getId())){
            return ResultDto.success("缺少参数");
        }
        if(Objects.isNull(modTestModuleDto.getPrdHost())||Objects.isNull(modTestModuleDto.getPreHost())
                ||Objects.isNull(modTestModuleDto.getSitHost())){
            return ResultDto.success("缺少参数");
        }

        Integer id = modTestModuleDto.getId();
        if(Objects.isNull(id)){
            return ResultDto.success("模块Id不能为空");
        }

        String name = modTestModuleDto.getModuleName();
        if(StringUtils.isEmpty(name)){
            return ResultDto.success("模块名称不能为空");
        }

        TestModule testModule = new TestModule();
        CopyUtil.copyPropertiesCglib(modTestModuleDto, testModule);

        ResultDto<TestModule> resultDto = testModuleService.update(testModule);
        return resultDto;
    }

    /**
     *
     * @param moduleId
     * @return
     */
    @ApiOperation(value = "根据ModuleId删除")
    @DeleteMapping("/{moduleId}")
    public ResultDto<TestModule> delete(HttpServletRequest request, @PathVariable Integer moduleId){

        log.info("根据ModuleId删除-入参= "+ moduleId);

        if(Objects.isNull(moduleId)){
            return ResultDto.success("ModuleId不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestModule> resultDto = testModuleService.delete(moduleId, tokenDto);
        return resultDto;
    }

    /**
     *
     * @param pageTableRequest
     * @return
     */
    @ApiOperation(value = "模块列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<TestModule>> list(HttpServletRequest request, PageTableRequest<QueTestModuleListDto> pageTableRequest){

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        log.info("列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest));

        QueTestModuleListDto params = pageTableRequest.getParams();
        if(Objects.isNull(params)){
            params = new QueTestModuleListDto();
        }
        //params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);

        ResultDto<PageTableResponse<TestModule>> responseResultDto = testModuleService.list(pageTableRequest);
        return responseResultDto;
    }

    /**
     *
     * @param moduleId
     * @return
     */
    @ApiOperation(value = "根据ModuleId查询")
    @GetMapping("/{moduleId}")
    public ResultDto<TestModule> getById(HttpServletRequest request, @PathVariable Integer moduleId){

        log.info("根据moduleId查询-入参= "+ moduleId);

        if(Objects.isNull(moduleId)){
            return ResultDto.success("ModuleId不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestModule> resultDto = testModuleService.getById(moduleId, tokenDto.getUserId());
        return resultDto;
    }

}
