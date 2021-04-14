package com.my.easytest.service.impl;

import com.my.easytest.constants.Constants;
import com.my.easytest.dao.TestJenkinsMapper;
import com.my.easytest.dao.TestTaskMapper;
import com.my.easytest.dto.AllureReportDto;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.dto.task.TaskDataDto;
import com.my.easytest.dto.task.TaskReportDto;
import com.my.easytest.entity.TestJenkins;
import com.my.easytest.entity.TestTask;
import com.my.easytest.service.TestReportService;
import com.my.easytest.util.ReportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TestReportServiceImpl implements TestReportService {

	@Autowired
	private TestTaskMapper testTaskMapper;

	@Autowired
	private TestJenkinsMapper testJenkinsMapper;

	/**
	 * 获取allure报告
	 *
	 * @param tokenDto
	 * @param taskId
	 * @return
	 */
	@Override
	public ResultDto<AllureReportDto> getAllureReport(TokenDto tokenDto, Integer taskId) {

		TestTask queryTestTask = new TestTask();

		queryTestTask.setId(taskId);
		queryTestTask.setCreateUserId(tokenDto.getUserId());

		TestTask resultTestTask = testTaskMapper.selectOne(queryTestTask);

		if(Objects.isNull(resultTestTask)){
			return ResultDto.fail("测试任务不存在 "+taskId);
		}

		String buildUrl = resultTestTask.getBuildUrl();

		if(StringUtils.isEmpty(buildUrl)){
			return ResultDto.fail("测试任务的构建地址不存在 "+taskId);
		}

		Integer testJenkinsId = resultTestTask.getTestJenkinsId();

		if(Objects.isNull(testJenkinsId)){
			return ResultDto.fail("测试任务的jenkinsId不存在 "+taskId);
		}

		TestJenkins queryTestJenkins = new TestJenkins();
		queryTestJenkins.setId(testJenkinsId);
		queryTestJenkins.setCreateUserId(tokenDto.getUserId());

		TestJenkins resultTestJenkins = testJenkinsMapper.selectOne(queryTestJenkins);

		String allureReportUrl = ReportUtil.getAllureReportUrl(buildUrl, resultTestJenkins, true);

		AllureReportDto allureReportDto = new AllureReportDto();
		allureReportDto.setTaskId(taskId);
		allureReportDto.setAllureReportUrl(allureReportUrl);

		return ResultDto.success("成功", allureReportDto);
	}

	/**
	 * 根据任务类型获取任务统计信息
	 *
	 * @param tokenDto
	 * @return
	 */
	@Override
	public ResultDto<TaskReportDto> getTaskByType(TokenDto tokenDto) {

		TaskReportDto taskReportDto = new TaskReportDto();

		Integer taskSum = 0;

		List<TaskDataDto>  taskDataDtoList = new ArrayList<TaskDataDto>(); // todo testTaskMapper.getTaskByType(tokenDto.getUserId());

		if(Objects.isNull(taskDataDtoList) || taskDataDtoList.size()==0){
			ResultDto.fail("无数据");
		}

		List<TaskDataDto>  newtTaskDataDtoList = new ArrayList<>();

		for (TaskDataDto taskDataDto:taskDataDtoList) {

			Integer taskKey = taskDataDto.getTaskKey();
			if(Objects.isNull(taskKey)){
				taskKey = 0;
			}

			if(0==taskKey){
				taskDataDto.setDesc("无匹配任务");
			}
			if(Constants.TASK_TYPE_ONE.equals(taskKey)){
				taskDataDto.setDesc("普通测试任务");
			}
			if(Constants.TASK_TYPE_TWO.equals(taskKey)){
				taskDataDto.setDesc("一键执行测试的任务");
			}

			taskSum = taskSum + taskDataDto.getTaskCount();

			newtTaskDataDtoList.add(taskDataDto);

		}

		taskReportDto.setTaskSum(taskSum);
		taskReportDto.setTaskDataDtoList(newtTaskDataDtoList);

		return ResultDto.success("成功",taskReportDto);
	}

	/**
	 * 根据任务状态获取任务统计信息
	 *
	 * @param tokenDto
	 * @return
	 */
	@Override
	public ResultDto<TaskReportDto> getTaskByStatus(TokenDto tokenDto) {
		TaskReportDto taskReportDto = new TaskReportDto();

		Integer taskSum = 0;

		List<TaskDataDto>  taskDataDtoList = new ArrayList<TaskDataDto>(); // todo testTaskMapper.getTaskByStatus(tokenDto.getUserId());

		if(Objects.isNull(taskDataDtoList) || taskDataDtoList.size()==0){
			ResultDto.fail("无数据");
		}

		List<TaskDataDto>  newtTaskDataDtoList = new ArrayList<>();

		for (TaskDataDto taskDataDto:taskDataDtoList) {

			Integer taskKey = taskDataDto.getTaskKey();
			if(Objects.isNull(taskKey)){
				taskKey = 0;
			}

			if(0==taskKey){
				taskDataDto.setDesc("无匹配任务");
			}
			if(Constants.STATUS_ONE.equals(taskKey)){
				taskDataDto.setDesc("新建");
			}
			if(Constants.STATUS_TWO.equals(taskKey)){
				taskDataDto.setDesc("执行中");
			}
			if(Constants.STATUS_THREE.equals(taskKey)){
				taskDataDto.setDesc("已完成");
			}

			taskSum = taskSum + taskDataDto.getTaskCount();

			newtTaskDataDtoList.add(taskDataDto);

		}

		taskReportDto.setTaskSum(taskSum);
		taskReportDto.setTaskDataDtoList(newtTaskDataDtoList);

		return ResultDto.success("成功",taskReportDto);
	}

	/**
	 * 任务中用例的数量统计信息
	 * @param tokenDto
	 * @param start 按时间倒叙开始序号
	 * @param end 按时间倒叙结束序号
	 * @return
	 */
	@Override
	public ResultDto<List<TestTask>> getTaskByCaseCount(TokenDto tokenDto, Integer start, Integer end) {

		List<TestTask> testTaskList = new ArrayList<>(); // todo testTaskMapper.getCaseCountByTask(tokenDto.getUserId(), start, end);

		if(Objects.isNull(testTaskList) || testTaskList.size()==0){
			return ResultDto.fail("无数据");
		}

		return ResultDto.success("成功", testTaskList);
	}
}
