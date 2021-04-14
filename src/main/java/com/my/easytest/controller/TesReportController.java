package com.my.easytest.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.easytest.common.TokenDb;
import com.my.easytest.constants.UserConstants;
import com.my.easytest.dto.AllureReportDto;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.dto.task.TaskReportDto;
import com.my.easytest.entity.TestTask;
import com.my.easytest.service.TestReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @Author G_ALLEN
 * @Date 2020/6/12 16:48
 **/
@Slf4j
@Api(tags = "报告管理")
@RestController
@RequestMapping("/report")
public class TesReportController {

    @Autowired
    private TestReportService testReportService;

    @Autowired
    private TokenDb tokenDb;

    @Value("${map.local.env}")
    private String mapLocalEnv;

    /**
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "获取allure报告")
    @GetMapping("/allureReport/{taskId}")
    public ResultDto<AllureReportDto> save(HttpServletRequest request, @PathVariable Integer taskId){

        log.info("报告管理-入参 id= "+ taskId);

        if(Objects.isNull(taskId)){
            return ResultDto.success("任务id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<AllureReportDto> resultDto = testReportService.getAllureReport(tokenDto, taskId);

        return resultDto;
    }

    /**
     * 根据任务类型获取任务统计信息 - 饼状图
     * @return
     */
    @ApiOperation(value = "根据任务类型获取任务统计信息 - 饼状图")
    @GetMapping("/taskByType")
    public ResultDto<TaskReportDto> update(HttpServletRequest request){

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        log.info("根据任务类型获取任务统计信息-入参= " + JSONObject.toJSONString(tokenDto));

        ResultDto<TaskReportDto> resultDto = testReportService.getTaskByType(tokenDto);

        return resultDto;
    }

    /**
     * 根据任务状态获取任务统计信息 - 饼状图
     * @return
     */
    @ApiOperation(value = "根据任务状态获取任务统计信息 - 饼状图")
    @GetMapping("/taskByStatus")
    public ResultDto<TaskReportDto> getById(HttpServletRequest request){

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        log.info("根据任务类型获取任务统计信息-入参= " + JSONObject.toJSONString(tokenDto));

        ResultDto<TaskReportDto> resultDto = testReportService.getTaskByStatus(tokenDto);

        return resultDto;
    }

    /**
     * 任务中用例的数量统计信息 - 折线图
     * @param request
     * @param start 按时间倒叙开始序号
     * @param end 按时间倒叙结束序号
     * @return
     */
    @ApiOperation(value = "任务中用例的数量统计信息 - 折线图")
    @GetMapping("/taskByCaseCount")
    public ResultDto<List<TestTask>> delete(HttpServletRequest request
            , @RequestParam(value = "start",required = false) Integer start
            , @RequestParam(value = "end",required = false) Integer end){

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        log.info("根据任务类型获取任务统计信息-入参= " + JSONObject.toJSONString(tokenDto));

        ResultDto<List<TestTask>> resultDto = testReportService.getTaskByCaseCount(tokenDto, start, end);

        return resultDto;
    }

    /**
     * @param param 任意数据，原样返回
     * @return
     */
    @ApiOperation(value = "演示map local")
    @GetMapping("/showMapLocal")
    public ResultDto<String> delete(@RequestParam(value = "param",required = false) String param){

        log.info("根据任务类型获取任务统计信息-入参= " + JSONObject.toJSONString(param));

        String mapLocalEnvStr = mapLocalEnv;

        if(!StringUtils.isEmpty(param)){
            mapLocalEnvStr = mapLocalEnv + "_" + param;
        }

        return ResultDto.success("成功",mapLocalEnvStr);
    }


}
