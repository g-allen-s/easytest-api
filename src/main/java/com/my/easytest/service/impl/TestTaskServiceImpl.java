package com.my.easytest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.my.easytest.exception.ServiceException;
import com.my.easytest.client.JenkinsClient;
import com.my.easytest.constants.Constants;
import com.my.easytest.dao.TestCaseMapper;
import com.my.easytest.dao.TestJenkinsMapper;
import com.my.easytest.dao.TestTaskCaseRelMapper;
import com.my.easytest.dao.TestTaskMapper;
import com.my.easytest.dto.*;
import com.my.easytest.dto.jenkins.OperateJenkinsJobDto;
import com.my.easytest.dto.task.AddTestTaskDto;
import com.my.easytest.dto.task.QueryTestTaskListDto;
import com.my.easytest.dto.task.TestTaskDto;
import com.my.easytest.entity.TestCase;
import com.my.easytest.entity.TestJenkins;
import com.my.easytest.entity.TestTask;
import com.my.easytest.entity.TestTaskCaseRel;
import com.my.easytest.service.TestTaskService;
import com.my.easytest.util.JenkinsUtil;
import com.my.easytest.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class TestTaskServiceImpl implements TestTaskService {

    @Autowired
    private TestTaskMapper testTaskMapper;

    @Autowired
    private TestJenkinsMapper testJenkinsMapper;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private TestTaskCaseRelMapper testTaskCaseRelMapper;

    @Autowired
    private JenkinsClient jenkinsClient;

    /**
     * 新增测试任务信息
     *
     * @param testTaskDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<TestTask> save(TestTaskDto testTaskDto, Integer taskType) {

        //StringBuilder testCommand = new StringBuilder();

        AddTestTaskDto testTask = testTaskDto.getTestTask();
        //List<Integer> caseIdList = testTaskDto.getCaseIdList();

        TestJenkins queryTestJenkins = new TestJenkins();
        queryTestJenkins.setId(testTask.getTestJenkinsId());
        queryTestJenkins.setCreateUserId(testTask.getCreateUserId());

        TestJenkins result = testJenkinsMapper.selectOne(queryTestJenkins);

        if(Objects.isNull(result)){
            return ResultDto.fail("Jenkins信息为空");
        }

        //List<TestCase> testCaseList = testCaseMapper.selectByIds(StrUtil.list2IdsStr(caseIdList));

        //makeTestCommand(testCommand, result, testCaseList);

        TestTask newTestTask = new TestTask();

        newTestTask.setTaskName(testTask.getTaskName());
        newTestTask.setTestJenkinsId(testTask.getTestJenkinsId());
        newTestTask.setJobName(testTask.getJobName());

        newTestTask.setCreateUserId(testTask.getCreateUserId());

        newTestTask.setTestCommand("");
        //newTestTask.setCaseCount(0);
        newTestTask.setStatus(Constants.STATUS_ONE);
        newTestTask.setCreateTime(new Date());
        newTestTask.setUpdateTime(new Date());

        testTaskMapper.insert(newTestTask);

//        if(Objects.nonNull(caseIdList) && caseIdList.size()>0){
//
//            List<TestTaskCaseRel> testTaskCaseList = new ArrayList<>();
//
//            for (Integer testCaseId:caseIdList) {
//
//                TestTaskCaseRel testTaskCaseRel = new TestTaskCaseRel();
//                testTaskCaseRel.setId(newTestTask.getId());
//                testTaskCaseRel.setCaseId(testCaseId);
//                testTaskCaseRel.setCreateUserId(newTestTask.getCreateUserId());
//                testTaskCaseRel.setCreateTime(new Date());
//                testTaskCaseRel.setUpdateTime(new Date());
//                testTaskCaseList.add(testTaskCaseRel);
//            }
//
//            log.info("=====测试任务详情保存-落库入参====："+ JSONObject.toJSONString(testTaskCaseList));
//            testTaskCaseRelMapper.insertList(testTaskCaseList);
//        }

        return ResultDto.success("成功", newTestTask);
    }

    /**
     * 删除测试任务信息
     *
     * @param taskId
     * @param createUserId
     * @return
     */
    @Override
    public ResultDto<TestTask> delete(Integer taskId, Integer createUserId) {
        TestTask queryTestTask = new TestTask();

        queryTestTask.setId(taskId);
        queryTestTask.setCreateUserId(createUserId);

        TestTask result = testTaskMapper.selectOne(queryTestTask);

        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }
        testTaskMapper.deleteByPrimaryKey(taskId);

        return ResultDto.success("成功");
    }

    /**
     * 修改测试任务信息
     *
     * @param testTask
     * @return
     */
    @Override
    public ResultDto<TestTask> update(TestTask testTask) {
        TestTask queryTestTask = new TestTask();

        queryTestTask.setId(testTask.getId());
        queryTestTask.setCreateUserId(testTask.getCreateUserId());

        TestTask result = testTaskMapper.selectOne(queryTestTask);

        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }

        result.setUpdateTime(new Date());
        result.setTaskName(testTask.getTaskName());
        result.setJobName(testTask.getJobName());
        result.setTestJenkinsId(testTask.getTestJenkinsId());

        testTaskMapper.updateByPrimaryKeySelective(result);

        return ResultDto.success("成功");
    }

    /**
     * 根据id查询
     *
     * @param taskId
     * @param createUserId
     * @return
     */
    @Override
    public ResultDto<TestTask> getById(Integer taskId, Integer createUserId) {
        TestTask queryTestTask = new TestTask();

        queryTestTask.setId(taskId);
        queryTestTask.setCreateUserId(createUserId);

        TestTask result = testTaskMapper.selectOne(queryTestTask);

        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            ResultDto.fail("未查到测试任务信息");
        }

        return ResultDto.success("成功", result);
    }

    /**
     * 查询测试任务信息列表
     *
     * @param pageTableRequest
     * @return
     */
    @Override
    public ResultDto<PageTableResponse<TestTask>> list(PageTableRequest<QueryTestTaskListDto> pageTableRequest) {
        QueryTestTaskListDto params = pageTableRequest.getParams();
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();

        //总数
        Integer recordsTotal = testTaskMapper.count(params);

        //分页查询数据
        List<TestTask> testJenkinsList = testTaskMapper.list(params, (pageNum - 1) * pageSize, pageSize);

        PageTableResponse<TestTask> testJenkinsPageTableResponse = new PageTableResponse<>();
        testJenkinsPageTableResponse.setRecordsTotal(recordsTotal);
        testJenkinsPageTableResponse.setData(testJenkinsList);

        return ResultDto.success("成功", testJenkinsPageTableResponse);
    }

    /**
     * 开始执行测试任务信息
     *
     * @param testTask
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, TestTask testTask) throws IOException {
        log.info("=====开始测试-Service请求入参====："+ JSONObject.toJSONString(requestInfoDto)+"+++++"+JSONObject.toJSONString(testTask));
        if(Objects.isNull(testTask)){
            return ResultDto.fail("测试任务参数不能为空");
        }

        Integer defaultJenkinsId = tokenDto.getDefaultJenkinsId();

        if(Objects.isNull(defaultJenkinsId)){
            return ResultDto.fail("未配置默认Jenkins");
        }

        TestJenkins queryTestJenkins = new TestJenkins();
        queryTestJenkins.setId(defaultJenkinsId);
        queryTestJenkins.setCreateUserId(tokenDto.getUserId());

        TestJenkins resultTestJenkins = testJenkinsMapper.selectOne(queryTestJenkins);

        if(Objects.isNull(resultTestJenkins)){
            return ResultDto.fail("默认Jenkins不存在或已失效");
        }

        TestTask queryTestTask = new TestTask();

        queryTestTask.setId(testTask.getId());
        queryTestTask.setCreateUserId(testTask.getCreateUserId());

        TestTask resultTestTask = testTaskMapper.selectOne(queryTestTask);


        if(Objects.isNull(resultTestTask)){
            String tips = "未查到测试任务";
            log.info(tips+ queryTestTask.getId());
            return ResultDto.fail(tips);
        }

        String testCommandStr =  testTask.getTestCommand();
        if(StringUtils.isEmpty(testCommandStr)){
            testCommandStr = resultTestTask.getTestCommand();
        }

        if(StringUtils.isEmpty(testCommandStr)){
            return ResultDto.fail("任务的测试命令不能为空");
        }

        //更新任务状态
        resultTestTask.setStatus(Constants.STATUS_TWO);
        testTaskMapper.updateByPrimaryKeySelective(resultTestTask);

        StringBuilder testCommand = new StringBuilder();

        //添加保存测试任务接口拼装的mvn test 命令
        testCommand.append(testCommandStr);
        testCommand.append(" \n");


        StringBuilder updateStatusUrl = JenkinsUtil.getUpdateTaskStatusUrl(requestInfoDto, resultTestTask);

        //构建参数组装
        Map<String, String> params = new HashMap<>();

        params.put("mytestBaseUrl",requestInfoDto.getBaseUrl());
        params.put("token",requestInfoDto.getToken());
        params.put("testCommand",testCommand.toString());
        params.put("updateStatusData",updateStatusUrl.toString());

        log.info("=====执行测试Job的构建参数组装====：" +JSONObject.toJSONString(params));
        log.info("=====执行测试Job的修改任务状态的数据组装====：" +updateStatusUrl);


        OperateJenkinsJobDto operateJenkinsJobDto = new OperateJenkinsJobDto();

        operateJenkinsJobDto.setParams(params);
        operateJenkinsJobDto.setTokenDto(tokenDto);
        operateJenkinsJobDto.setTestJenkins(resultTestJenkins);

        operateJenkinsJobDto.setParams(params);

        ResultDto resultDto = jenkinsClient.operateJenkinsJob(operateJenkinsJobDto);
        //此处抛出异常，阻止事务提交
        if(0 == resultDto.getResultCode()){
            throw new ServiceException("执行测试时异常-"+resultDto.getMessage());
        }
        return resultDto;
    }

    /**
     * 修改测试任务状态信息
     *
     * @param testTask
     * @return
     */
    @Override
    public ResultDto<TestTask> updateStatus(TestTask testTask) {
        TestTask queryTestTask = new TestTask();

        queryTestTask.setId(testTask.getId());
        queryTestTask.setCreateUserId(testTask.getCreateUserId());

        TestTask result = testTaskMapper.selectOne(queryTestTask);

        //如果为空，则提示
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }

        //如果任务已经完成，则不重复修改
        if(Constants.STATUS_THREE.equals(result.getStatus())){
            return ResultDto.fail("测试任务已完成，无需修改");
        }
        result.setUpdateTime(new Date());

        //仅状态为已完成时修改
        if(Constants.STATUS_THREE.equals(testTask.getStatus())){
            result.setBuildUrl(testTask.getBuildUrl());
            result.setStatus(Constants.STATUS_THREE);
            testTaskMapper.updateByPrimaryKey(result);
        }

        return ResultDto.success("成功");
    }

    /**
     *
     * @param testCommand
     * @param testCaseList
     */
    private void makeTestCommand(StringBuilder testCommand, TestJenkins testJenkins, List<TestCase> testCaseList) {

        //打印测试目录
        testCommand.append("pwd");
        testCommand.append("\n");

        if(Objects.isNull(testJenkins)){
            throw new ServiceException("组装测试命令时，Jenkins信息为空");
        }
        if(Objects.isNull(testCaseList) || testCaseList.size()==0){
            throw new ServiceException("组装测试命令时，测试用例列表信息为空");
        }

        String runCommand = testJenkins.getTestCommand();

        Integer commandRunCaseType = Integer.valueOf(testJenkins.getCommandType());
        String systemTestCommand = testJenkins.getTestCommand();

        if(StringUtils.isEmpty(systemTestCommand)){
            throw new ServiceException("组装测试命令时，运行的测试命令信息为空");
        }

        //默认文本类型
        if(Objects.isNull(commandRunCaseType)){
            commandRunCaseType = 1;
        }

        //文本类型
        if(commandRunCaseType==1){
            for (TestCase testCase :testCaseList) {
                //拼装命令前缀
                testCommand.append(systemTestCommand).append(" ");
                //拼装测试数据
                testCommand.append(testCase.getCaseData())
                .append("\n");
            }
        }
        //文件类型
        if(commandRunCaseType==2){

            String commandRunCaseSuffix = testJenkins.getCommandSuffix();

            if(StringUtils.isEmpty(commandRunCaseSuffix)){
                throw new ServiceException("组装测试命令且case为文件时，测试用例后缀名不能为空");
            }

            for (TestCase testCase :testCaseList) {

                //拼装下载文件的curl命令
                makeCurlCommand(testCommand, testCase, commandRunCaseSuffix);
                testCommand.append("\n");
                //拼装命令前缀
                testCommand.append(systemTestCommand).append(" ");
                //平台测试用例名称
                testCommand.append(testCase.getCaseName())
                        //拼装.分隔符
                        .append(".")
                        //拼装case文件后缀
                        .append(commandRunCaseSuffix)
                        .append(" || true")
                        .append("\n");
            }
        }



        log.info("testCommand.toString()== "+testCommand.toString() + "  runCommand== " + runCommand);


        testCommand.append("\n");
    }

    /**
     *  拼装下载文件的curl命令
     * @param testCommand
     * @param testCase
     * @param commandRunCaseSuffix
     */
    private void makeCurlCommand(StringBuilder testCommand, TestCase testCase, String commandRunCaseSuffix) {

        //通过curl命令获取测试数据并保存为文件
        testCommand.append("curl ")
                .append("-o ");

        String caseName = testCase.getCaseName();

        if(StringUtils.isEmpty(caseName)){
            caseName = "测试用例无测试名称";
        }

        testCommand.append(caseName)
                .append(".")
                .append(commandRunCaseSuffix)
                .append(" ${mytestBaseUrl}/testCase/data/")
                .append(testCase.getId())
                .append(" -H \"token: ${token}\" ");

        //本行命令执行失败，继续运行下面的命令行
        testCommand.append(" || true");

        testCommand.append("\n");
    }

}
