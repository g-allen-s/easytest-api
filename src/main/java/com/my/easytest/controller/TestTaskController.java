package com.my.easytest.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.easytest.common.TokenDb;
import com.my.easytest.constants.Constants;
import com.my.easytest.constants.UserConstants;
import com.my.easytest.dto.*;
import com.my.easytest.dto.task.*;
import com.my.easytest.entity.TestTask;
import com.my.easytest.service.TestTaskService;
import com.my.easytest.util.CopyUtil;
import com.my.easytest.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "测试任务管理")
@RestController
@RequestMapping("/task")
public class TestTaskController {

    @Autowired
    private TestTaskService testTaskService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param testTaskDto
     * @return
     */
    @ApiOperation(value = "添加测试任务")
    @PostMapping
    public ResultDto<TestTask> save(HttpServletRequest request, @RequestBody TestTaskDto testTaskDto){

        log.info("添加测试任务-入参= "+ JSONObject.toJSONString(testTaskDto));

        if(Objects.isNull(testTaskDto)){
            return ResultDto.fail("测试任务入参不能为空");
        }

        AddTestTaskDto addTestTaskDto = testTaskDto.getTestTask();

        if(Objects.isNull(addTestTaskDto)){
            return ResultDto.fail("测试任务不能为空");
        }

        if(Objects.isNull(addTestTaskDto.getTaskName())){
            return ResultDto.fail("测试任务名称不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        addTestTaskDto.setCreateUserId(tokenDto.getUserId());
        addTestTaskDto.setTestJenkinsId(tokenDto.getDefaultJenkinsId());

        ResultDto<TestTask> resultDto = testTaskService.save(testTaskDto, Constants.TASK_TYPE_ONE);
        return resultDto;
    }

    /**
     *
     * @param updateTestTaskDto
     * @return
     */
    @ApiOperation(value = "修改测试任务")
    @PutMapping
    public ResultDto<TestTask> update(HttpServletRequest request, @RequestBody UpdateTestTaskDto updateTestTaskDto){

        log.info("修改测试任务-入参= "+ JSONObject.toJSONString(updateTestTaskDto));

        if(Objects.isNull(updateTestTaskDto)){
            return ResultDto.fail("测试任务信息不能为空");
        }

        Integer taskId = updateTestTaskDto.getId();
        String name = updateTestTaskDto.getTaskName();

        if(Objects.isNull(taskId)){
            return ResultDto.fail("任务id不能为空");
        }


        if(StringUtils.isEmpty(name)){
            return ResultDto.fail("任务名称不能为空");
        }

        TestTask testTask = new TestTask();
        CopyUtil.copyPropertiesCglib(updateTestTaskDto, testTask);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        testTask.setCreateUserId(tokenDto.getUserId());

        ResultDto<TestTask> resultDto = testTaskService.update(testTask);
        return resultDto;
    }

    /**
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "根据任务Id查询")
    @GetMapping("/{taskId}")
    public ResultDto<TestTask> getById(HttpServletRequest request, @PathVariable Integer taskId){

        log.info("根据任务Id查询-入参= "+ taskId);

        if(Objects.isNull(taskId)){
            return ResultDto.success("任务Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestTask> resultDto = testTaskService.getById(taskId, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "根据任务Id删除")
    @DeleteMapping("/{taskId}")
    public ResultDto<TestTask> delete(HttpServletRequest request, @PathVariable Integer taskId){

        log.info("根据任务Id删除-入参= "+ taskId);

        if(Objects.isNull(taskId)){
            return ResultDto.success("任务Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<TestTask> resultDto = testTaskService.delete(taskId, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param pageTableRequest
     * @return
     */
    @ApiOperation(value = "列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<TestTask>> list(HttpServletRequest request, PageTableRequest<QueryTestTaskListDto> pageTableRequest){

        log.info("任务列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest));

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        QueryTestTaskListDto params = pageTableRequest.getParams();

        if(Objects.isNull(params)){
            params = new QueryTestTaskListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);

        ResultDto<PageTableResponse<TestTask>> responseResultDto = testTaskService.list(pageTableRequest);
        return responseResultDto;
    }

    /**
     *
     * @param updateTestTaskStatusDto
     * @return
     */
    @ApiOperation(value = "修改测试任务")
    @PutMapping("status")
    public ResultDto<TestTask> updateStatus(HttpServletRequest request, @RequestBody UpdateTestTaskStatusDto updateTestTaskStatusDto){

        log.info("修改测试任务状态-入参= "+ JSONObject.toJSONString(updateTestTaskStatusDto));

        if(Objects.isNull(updateTestTaskStatusDto)){
            return ResultDto.success("修改测试任务状态信息不能为空");
        }

        Integer taskId = updateTestTaskStatusDto.getId();
        String buildUrl = updateTestTaskStatusDto.getBuildUrl();
        Integer status = updateTestTaskStatusDto.getStatus();

        if(Objects.isNull(taskId)){
            return ResultDto.success("任务id不能为空");
        }

        if(StringUtils.isEmpty(buildUrl)){
            return ResultDto.success("任务构建地址不能为空");
        }

        if(StringUtils.isEmpty(status)){
            return ResultDto.success("任务状态码不能为空");
        }

        TestTask testTask = new TestTask();

        testTask.setId(taskId);
        testTask.setBuildUrl(buildUrl);
        testTask.setStatus(status);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        testTask.setCreateUserId(tokenDto.getUserId());

        ResultDto<TestTask> resultDto = testTaskService.updateStatus(testTask);
        return resultDto;
    }

    /**
     * 开始测试接口
     * @param startTestDto
     * @return
     * @throws Exception
     */
    @PostMapping("start")
    @ApiOperation(value = "开始测试", notes = "开始测试-说明", httpMethod = "POST", response = ResultDto.class)
    public ResultDto testStart(HttpServletRequest request
            ,@ApiParam(name="修改测试任务状态对象", required=true)@RequestBody StartTestDto startTestDto) throws Exception {
        log.info("=====开始测试-请求入参====："+ JSONObject.toJSONString(startTestDto));

        if(Objects.isNull(startTestDto)){
            return ResultDto.fail("开始测试请求不能为空");
        }
        if(Objects.isNull(startTestDto.getTaskId())){
            return ResultDto.fail("任务id不能为空");
        }

        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        log.info("token== "+token);

        TestTask testTask = new TestTask();
        testTask.setId(startTestDto.getTaskId());
        testTask.setTestCommand(startTestDto.getTestCommand());
        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        testTask.setCreateUserId(tokenDto.getUserId());
        testTask.setTestJenkinsId(tokenDto.getDefaultJenkinsId());


        String url = request.getRequestURL().toString();
        log.info("请求地址== "+url);
        url = StrUtil.getHostAndPort(request.getRequestURL().toString());

        RequestInfoDto requestInfoDto = new RequestInfoDto();
        requestInfoDto.setBaseUrl(url);
        requestInfoDto.setRequestUrl(url);
        requestInfoDto.setToken(token);
        log.info("requestInfoDto== "+ JSONObject.toJSONString(requestInfoDto));
        return testTaskService.startTask(tokenDto, requestInfoDto, testTask);
    }

}
