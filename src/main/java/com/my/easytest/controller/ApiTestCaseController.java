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
import com.my.easytest.model.ApiTestGen;
import com.my.easytest.service.ApiTestCaseService;
import com.my.easytest.util.CopyUtil;
import com.my.easytest.util.DateUtil;
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
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author G_ALLEN
 * @Date 2021/1/11 16:48
 **/
@Slf4j
@Api(tags = "API接口用例管理")
@RestController
@RequestMapping("/apiTestCase")
public class ApiTestCaseController {

    @Autowired
    private ApiTestCaseService apiTestCaseService;

    @Autowired
    private TokenDb tokenDb;

    /**
     *
     * @param newTestCaseDto
     * @return
     */
    @ApiOperation(value = "添加API接口用例")
    @PostMapping
    public ResultDto<ApiTestCase> save(HttpServletRequest request, @RequestBody NewTestCaseDto newTestCaseDto){

        log.info("添加API接口用例-入参= "+ JSONObject.toJSONString(newTestCaseDto));

        if(Objects.isNull(newTestCaseDto)){
            return ResultDto.success("用例信息不能为空");
        }

        if(Objects.isNull(newTestCaseDto.getApiModule())||Objects.isNull(newTestCaseDto.getApiTcPriority())){
            return ResultDto.success("缺少参数");
        }

        String apiTcName = newTestCaseDto.getApiTcName();
        if(StringUtils.isEmpty(apiTcName)){
            return ResultDto.success("用例名称不能为空");
        }

        String apiTcCode = newTestCaseDto.getApiTcCode();
        if(StringUtils.isEmpty(apiTcCode)){
            return ResultDto.success("用例编码不能为空");
        }

        ApiTestCase apiTestCase = new ApiTestCase();
        CopyUtil.copyPropertiesCglib(newTestCaseDto, apiTestCase);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        apiTestCase.setCreateUserId(tokenDto.getUserId());

        ResultDto<ApiTestCase> resultDto = apiTestCaseService.save(apiTestCase);
        return resultDto;
    }

    /**
     *
     * @param modTestCaseDto
     * @return
     */
    @ApiOperation(value = "更新API接口用例")
    @PutMapping
    public ResultDto<ApiTestCase> update(HttpServletRequest request, @RequestBody ModTestCaseDto modTestCaseDto){

        log.info("更新API接口用例-入参= " + JSONObject.toJSONString(modTestCaseDto));

        if(Objects.isNull(modTestCaseDto)){
            return ResultDto.success("用例信息不能为空");
        }

        if(Objects.isNull(modTestCaseDto.getId())){
            return ResultDto.success("缺少参数");
        }
        if(Objects.isNull(modTestCaseDto.getApiModule())||Objects.isNull(modTestCaseDto.getApiTcPriority())){
            return ResultDto.success("缺少参数");
        }

        String apiTcName = modTestCaseDto.getApiTcName();
        if(StringUtils.isEmpty(apiTcName)){
            return ResultDto.success("用例名称不能为空");
        }

        String apiTcCode = modTestCaseDto.getApiTcCode();
        if(StringUtils.isEmpty(apiTcCode)){
            return ResultDto.success("用例编码不能为空");
        }

        ApiTestCase apiTestCase = new ApiTestCase();
        CopyUtil.copyPropertiesCglib(modTestCaseDto, apiTestCase);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<ApiTestCase> resultDto = apiTestCaseService.update(apiTestCase, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据删除")
    @DeleteMapping("/{id}")
    public ResultDto<ApiTestCase> delete(HttpServletRequest request, @PathVariable Integer id){

        log.info("根据TestCaseId删除-入参= "+ id);

        if(Objects.isNull(id)){
            return ResultDto.success("TestCaseId不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<ApiTestCase> resultDto = apiTestCaseService.delete(id, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param pageTableRequest
     * @return
     */
    @ApiOperation(value = "用例列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<ApiTestCase>> list(HttpServletRequest request, PageTableRequest<QueTestCaseListDto> pageTableRequest){

        log.info("列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest));

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        QueTestCaseListDto params = pageTableRequest.getParams();
        if(Objects.isNull(params)){
            params = new QueTestCaseListDto();
        }
        params.setCreateUserId(tokenDto.getUserId());
        pageTableRequest.setParams(params);

        ResultDto<PageTableResponse<ApiTestCase>> responseResultDto = apiTestCaseService.list(pageTableRequest);
        return responseResultDto;
    }

    /**
     *
     * @param preTestDto
     * @return
     */
    @ApiOperation(value = "添加API接口前置测试")
    @PostMapping("/savePreTest")
    public ResultDto<ApiPreTest> savePreTest(HttpServletRequest request, @RequestBody PreTestDto preTestDto){

        log.info("添加API接口前置测试-入参= "+ JSONObject.toJSONString(preTestDto));

        if(Objects.isNull(preTestDto)){
            return ResultDto.success("测试信息不能为空");
        }

        if(Objects.isNull(preTestDto.getPreTestMethod())||Objects.isNull(preTestDto.getPreTestTarget())
                ||StringUtils.isEmpty(preTestDto.getRequestParam())||StringUtils.isEmpty(preTestDto.getSamplesParam())){
            return ResultDto.success("缺少参数");
        }

        ApiPreTest preTest = new ApiPreTest();
        CopyUtil.copyPropertiesCglib(preTestDto, preTest);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<ApiPreTest> resultDto = apiTestCaseService.save(preTest, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "根据uid删除前置测试")
    @DeleteMapping("/deletePreTest/{uid}")
    public ResultDto<ApiPreTest> deletePreTest(HttpServletRequest request, @PathVariable String uid){

        log.info("根据uid删除前置测试-入参= "+ uid);

        if(Objects.isNull(uid)){
            return ResultDto.success("Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<ApiPreTest> resultDto = apiTestCaseService.deletePreTest(uid, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param testActionDto
     * @return
     */
    @ApiOperation(value = "添加API接口测试步骤")
    @PostMapping("/saveTestAction")
    public ResultDto<ApiTestAction> saveTestAction(HttpServletRequest request, @RequestBody NewApiTestActionDto testActionDto){

        log.info("添加API接口测试步骤-入参= "+ JSONObject.toJSONString(testActionDto));

        if(Objects.isNull(testActionDto)){
            return ResultDto.success("测试信息不能为空");
        }

        if(Objects.isNull(testActionDto.getTcId())||Objects.isNull(testActionDto.getApiId())
                ||StringUtils.isEmpty(testActionDto.getTestActionName())){
            return ResultDto.success("缺少参数");
        }

        ApiTestAction testAction = new ApiTestAction();
        CopyUtil.copyPropertiesCglib(testActionDto, testAction);

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
        testAction.setCreateUserId(tokenDto.getUserId());

        ResultDto<ApiTestAction> resultDto = apiTestCaseService.save(testAction);
        return resultDto;
    }

    /**
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "根据uid删除测试步骤")
    @DeleteMapping("/deleteTestAction/{uid}")
    public ResultDto<ApiTestAction> deleteTestAction(HttpServletRequest request, @PathVariable String uid){

        log.info("根据uid删除测试步骤-入参= "+ uid);

        if(Objects.isNull(uid)){
            return ResultDto.success("Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<ApiTestAction> resultDto = apiTestCaseService.deleteTestAction(uid, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param apiTestAssertionDto
     * @return
     */
    @ApiOperation(value = "添加API接口测试断言")
    @PostMapping("/saveTestAssertion")
    public ResultDto<ApiTestAssertion> saveTestAssertion(HttpServletRequest request, @RequestBody ApiTestAssertionDto apiTestAssertionDto){

        log.info("添加API接口测试断言-入参= "+ JSONObject.toJSONString(apiTestAssertionDto));

        if(Objects.isNull(apiTestAssertionDto)){
            return ResultDto.success("测试信息不能为空");
        }

        if(Objects.isNull(apiTestAssertionDto.getTestActionId())||Objects.isNull(apiTestAssertionDto.getAssertionType())
                ||StringUtils.isEmpty(apiTestAssertionDto.getAssertionKey())||StringUtils.isEmpty(apiTestAssertionDto.getAssertionValue())){
            return ResultDto.success("缺少参数");
        }

        ApiTestAssertion testAssertion = new ApiTestAssertion();
        CopyUtil.copyPropertiesCglib(apiTestAssertionDto, testAssertion);

        //TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<ApiTestAssertion> resultDto = apiTestCaseService.save(testAssertion);
        return resultDto;
    }

    /**
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "根据uid删除测试断言")
    @DeleteMapping("/deleteTestAssertion/{uid}")
    public ResultDto<ApiTestAssertion> deleteTestAssertion(HttpServletRequest request, @PathVariable String uid){

        log.info("根据uid删除测试断言-入参= "+ uid);

        if(Objects.isNull(uid)){
            return ResultDto.success("Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<ApiTestAssertion> resultDto = apiTestCaseService.deleteTestAssertion(uid, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param apiTestSampleDto
     * @return
     */
    @ApiOperation(value = "添加API接口测试取样")
    @PostMapping("/saveTestSample")
    public ResultDto<ApiTestSample> saveTestSample(HttpServletRequest request, @RequestBody ApiTestSampleDto apiTestSampleDto){

        log.info("添加API接口测试取样-入参= "+ JSONObject.toJSONString(apiTestSampleDto));

        if(Objects.isNull(apiTestSampleDto)){
            return ResultDto.success("测试信息不能为空");
        }

        if(Objects.isNull(apiTestSampleDto.getTestActionId())
                ||StringUtils.isEmpty(apiTestSampleDto.getSampleSource())
                ||StringUtils.isEmpty(apiTestSampleDto.getSampleTarget())){
            return ResultDto.success("缺少参数");
        }

        ApiTestSample testSample = new ApiTestSample();
        CopyUtil.copyPropertiesCglib(apiTestSampleDto, testSample);

        //TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<ApiTestSample> resultDto = apiTestCaseService.save(testSample);
        return resultDto;
    }

    /**
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "根据uid删除测试取样")
    @DeleteMapping("/deleteTestSample/{uid}")
    public ResultDto<ApiTestSample> deleteTestSample(HttpServletRequest request, @PathVariable String uid){

        log.info("根据uid删除测试取样-入参= "+ uid);

        if(Objects.isNull(uid)){
            return ResultDto.success("Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<ApiTestSample> resultDto = apiTestCaseService.deleteTestSample(uid, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param apiTestDataPool
     * @return
     */
    @ApiOperation(value = "添加API接口测试数据")
    @PostMapping("/saveTestData")
    public ResultDto<ApiTestDataPool> saveTestData(HttpServletRequest request, @RequestBody ApiTestDataPoolDto apiTestDataPool){

        log.info("添加API接口测试取样-入参= "+ JSONObject.toJSONString(apiTestDataPool));

        if(Objects.isNull(apiTestDataPool)){
            return ResultDto.success("测试信息不能为空");
        }

        if(Objects.isNull(apiTestDataPool.getTcId())||StringUtils.isEmpty(apiTestDataPool.getDatapoolName())
                ||StringUtils.isEmpty(apiTestDataPool.getDatapoolParam())){
            return ResultDto.success("缺少参数");
        }

        ApiTestDataPool testDataPool = new ApiTestDataPool();
        CopyUtil.copyPropertiesCglib(apiTestDataPool, testDataPool);

        //TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<ApiTestDataPool> resultDto = apiTestCaseService.save(testDataPool);
        return resultDto;
    }

    /**
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "根据uid删除测试数据")
    @DeleteMapping("/deleteTestData/{uid}")
    public ResultDto<ApiTestDataPool> deleteTestData(HttpServletRequest request, @PathVariable String uid){

        log.info("根据uid删除测试数据-入参= "+ uid);

        if(Objects.isNull(uid)){
            return ResultDto.success("Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto<ApiTestDataPool> resultDto = apiTestCaseService.deleteTestDataPool(uid, tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param runApiTestDto
     * @return
     */
    @ApiOperation(value = "运行API接口测试")
    @GetMapping("/run")
    public ResultDto run(HttpServletRequest request, RunApiTestDto runApiTestDto) {
        if(Objects.isNull(runApiTestDto)||Objects.isNull(runApiTestDto.getTcId())){
            return ResultDto.success("缺少参数");
        }

        if(StringUtils.isEmpty(runApiTestDto.getEnv())){
            return ResultDto.success("缺少参数");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        ResultDto resultDto = apiTestCaseService.run(runApiTestDto.getTcId(), runApiTestDto.getEnv(), tokenDto.getUserId());
        return resultDto;
    }

    /**
     *
     * @param runMultipleApiTestDto
     * @return
     */
    @ApiOperation(value = "运行多个API接口测试")
    @GetMapping("/runMultiple")
    public ResultDto runMultiple(HttpServletRequest request, RunMultipleApiTestDto runMultipleApiTestDto) {
        if (Objects.isNull(runMultipleApiTestDto)) {
            return ResultDto.success("缺少参数");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        List<Integer> tcIds = runMultipleApiTestDto.getTcIds();

        String env = runMultipleApiTestDto.getEnv();

        String runParallel = runMultipleApiTestDto.getRunParallel();
        if (runParallel.equals("serial"))  {
            ResultDto resultDto = new ResultDto();
            resultDto.setAsSuccess();
            resultDto.setMessage("运行成功！");
            Iterator<Integer> iterator = tcIds.iterator();
            while(iterator.hasNext()) {
                Integer tcId = iterator.next();
                System.out.println("=== run one-by-one at [" + new Date() + "] of test::" + tcId);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ResultDto result = apiTestCaseService.run(tcId, env, tokenDto.getUserId());
                System.out.println("=== ["+ tcId + "] run result: " + result.getResultCode());
                if(result.getResultCode() == 0) {
                    resultDto.setAsFailure();
                    resultDto.setMessage(result.getMessage());
                }
            }
            return resultDto;
        }
        // run if in parallel
        else {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>()); // newCachedThreadPool
            ResultDto resultDto = new ResultDto();
            resultDto.setAsSuccess();
            resultDto.setMessage("运行成功！");
            Iterator<Integer> iterator = tcIds.iterator();
            while(iterator.hasNext()) {
                final Integer tcId = iterator.next();
                threadPoolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("=== run in parallel at [" + new Date() + "] of test::" + tcId);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ResultDto result = apiTestCaseService.run(tcId, env, tokenDto.getUserId());
                        System.out.println("=== ["+ tcId + "] run result: " + result.getResultCode());
                        if(result.getResultCode() == 0) {
                            resultDto.setAsFailure();
                            resultDto.setMessage(result.getMessage());
                        }
                    }
                });
            }
            return resultDto;
        }
    }

    /**
     *
     * @param runApiTestDto
     * @return
     */
    @ApiOperation(value = "定时运行API接口测试")
    @GetMapping("/runOnSchedule")
    public ResultDto runOnSchedule(HttpServletRequest request, RunApiTestDto runApiTestDto) {
        final Integer tcId = runApiTestDto.getTcId();
        final String env = runApiTestDto.getEnv();

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("=== schedule to run at [" + new Date() + "]");
                ResultDto testResult = apiTestCaseService.run(tcId, env, tokenDto.getUserId());
                System.out.println("=== run result: " + testResult.getResultCode());
            }
        };

        Timer timer = new Timer("timer", Boolean.FALSE);
        if (Objects.nonNull(runApiTestDto.getDelayValue()) && Objects.nonNull(runApiTestDto.getDelayUnit())) {
            long delay = 0;
            if ("h".equals(runApiTestDto.getDelayUnit())) {
                delay = Integer.parseInt(runApiTestDto.getDelayValue()) * 60 * 60 * 1000;
            } else if ("m".equals(runApiTestDto.getDelayUnit())) {
                delay = Integer.parseInt(runApiTestDto.getDelayValue()) * 60 * 1000;
            } else if ("s".equals(runApiTestDto.getDelayUnit())) {
                delay = Integer.parseInt(runApiTestDto.getDelayValue()) * 1000;
            }
            timer.schedule(timerTask, delay);

        } else if (Objects.nonNull(runApiTestDto.getScheduledTime())) {
            Date tillDate = DateUtil.StringToDatetime(runApiTestDto.getScheduledTime());
            timer.schedule(timerTask, tillDate);
        }
        return ResultDto.success("定时测试计划已设置！");
    }

    /**
     *
     * @param runMultipleApiTestDto
     * @return
     */
    @ApiOperation(value = "定时运行多个API接口测试")
    @GetMapping("/runMultipleOnSchedule")
    public ResultDto runMultipleOnSchedule(HttpServletRequest request, RunMultipleApiTestDto runMultipleApiTestDto) {
        if (Objects.isNull(runMultipleApiTestDto)) {
            return ResultDto.success("缺少参数");
        }

        TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));

        List<Integer> tcIds = runMultipleApiTestDto.getTcIds();

        String env = runMultipleApiTestDto.getEnv();

        // translate delay which should over than 0
        long delay = 0;
        if (Objects.nonNull(runMultipleApiTestDto.getDelayValue()) && Objects.nonNull(runMultipleApiTestDto.getDelayUnit())) {
            if ("h".equals(runMultipleApiTestDto.getDelayUnit())) {
                delay = Integer.parseInt(runMultipleApiTestDto.getDelayValue()) * 60 * 60 * 1000;
            } else if ("m".equals(runMultipleApiTestDto.getDelayUnit())) {
                delay = Integer.parseInt(runMultipleApiTestDto.getDelayValue()) * 60 * 1000;
            } else if ("s".equals(runMultipleApiTestDto.getDelayUnit())) {
                delay = Integer.parseInt(runMultipleApiTestDto.getDelayValue()) * 1000;
            }
        } else if (!StringUtils.isEmpty(runMultipleApiTestDto.getScheduledTime())) {
            long tillTime = (DateUtil.StringToDatetime(runMultipleApiTestDto.getScheduledTime())).getTime();
            long currentTime = (new Date()).getTime();
            delay = tillTime - currentTime;
        }
        if (delay <= 0)
            return ResultDto.success("delay time reached error");
        // run if in serial
        String runParallel = runMultipleApiTestDto.getRunParallel();
        if (runParallel.equals("serial"))  {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Iterator<Integer> iterator = tcIds.iterator();
                    while(iterator.hasNext()) {
                        Integer tcId = iterator.next();
                        System.out.println("=== schedule to run one-by-one at [" + new Date() + "] of test::" + tcId);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ResultDto result = apiTestCaseService.run(tcId, env, tokenDto.getUserId());
                        System.out.println("=== ["+ tcId + "] run result: " + result.getResultCode());
                    }
                }
            };
            Timer timer = new Timer("timer", Boolean.FALSE);
            timer.schedule(timerTask, delay);
        }
        // run if in parallel
        else {
            ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(10);
            Iterator<Integer> iterator = tcIds.iterator();
            while(iterator.hasNext()) {
                final Integer tcId = iterator.next();
                threadPoolExecutor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("=== schedule to run in parallel at [" + new Date() + "] of test::" + tcId);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ResultDto result = apiTestCaseService.run(tcId, env, tokenDto.getUserId());
                        System.out.println("=== ["+ tcId + "] run result: " + result.getResultCode());
                    }
                }, delay, TimeUnit.MILLISECONDS);
            }
        }
        // return
        return ResultDto.success("定时测试计划已设置！");
    }

    /**
     *
     * @param importApiTestDto
     * @return
     */
    @ApiOperation(value = "导入接口测试")
    @PostMapping("file")
    public ResultDto importApiTest(HttpServletRequest request, @RequestParam("modelFile") MultipartFile modelFile, ImportApiTestDto importApiTestDto) {
        String fileName = importApiTestDto.getFileName();
        if (fileName.endsWith(".yaml")) {
            Yaml yaml = new Yaml();
            try {
                InputStream inputStream = modelFile.getInputStream();
                ApiTestGen apiTestGen = yaml.loadAs(inputStream, ApiTestGen.class);
                apiTestGen.setModuleId(importApiTestDto.getModuleId());
                inputStream.close();

                TokenDto tokenDto = tokenDb.getTokenDto(request.getHeader(UserConstants.LOGIN_TOKEN));
                ResultDto resultDto = apiTestCaseService.generateApiTestKit(apiTestGen, tokenDto.getUserId());
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
