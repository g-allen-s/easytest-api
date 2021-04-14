package com.my.easytest.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.easytest.common.TokenDb;
import com.my.easytest.constants.UserConstants;
import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.testcase.TestTaskCaseRelDetailDto;
import com.my.easytest.dto.testcase.QueryTestTaskCaseRelListDto;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.service.TestTaskCaseRelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author G_ALLEN
 * @Date 2020/6/12 16:48
 **/
@Slf4j
@Api(tags = "任务与用例关联管理")
@RestController
@RequestMapping("/taskCaseRel")
public class TestTaskCaseRelController {

    @Autowired
    private TestTaskCaseRelService testTaskCaseRelService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param pageTableRequest
     * @return
     */
    @ApiOperation(value = "列表查询")
    @GetMapping("/listDetail")
    public ResultDto<PageTableResponse<TestTaskCaseRelDetailDto>> list(HttpServletRequest request, PageTableRequest<QueryTestTaskCaseRelListDto> pageTableRequest){

        log.info("任务与用例关联管理 列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest));

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        QueryTestTaskCaseRelListDto params = pageTableRequest.getParams();

        if(Objects.isNull(params)){
            params = new QueryTestTaskCaseRelListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);

        ResultDto<PageTableResponse<TestTaskCaseRelDetailDto>> responseResultDto = testTaskCaseRelService.listDetail(pageTableRequest);
        return responseResultDto;
    }

}
