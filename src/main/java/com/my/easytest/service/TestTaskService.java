package com.my.easytest.service;

import com.my.easytest.dto.*;
import com.my.easytest.dto.task.QueryTestTaskListDto;
import com.my.easytest.dto.task.TestTaskDto;
import com.my.easytest.entity.TestTask;

import java.io.IOException;

public interface TestTaskService {

	/**
	 *  新增测试任务信息
	 * @param testTaskDto
	 * @return
	 */
	ResultDto<TestTask> save(TestTaskDto testTaskDto, Integer taskType);

	/**
	 *  删除测试任务信息
	 * @param taskId
	 * @param createUserId
	 * @return
	 */
	ResultDto<TestTask> delete(Integer taskId, Integer createUserId);

	/**
	 *  修改测试任务信息
	 * @param testTask
	 * @return
	 */
	ResultDto<TestTask> update(TestTask testTask);

	/**
	 *  根据id查询
	 * @param taskId
	 * @return
	 */
	ResultDto<TestTask> getById(Integer taskId, Integer createUserId);

	/**
	 *  查询测试任务信息列表
	 * @param pageTableRequest
	 * @return
	 */
	ResultDto<PageTableResponse<TestTask>> list(PageTableRequest<QueryTestTaskListDto> pageTableRequest);

	/**
	 *  开始执行测试任务信息
	 * @param testTask
	 * @return
	 */
	ResultDto startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, TestTask testTask) throws IOException;

	/**
	 *  修改测试任务状态信息
	 * @param testTask
	 * @return
	 */
	ResultDto<TestTask> updateStatus(TestTask testTask);


}
