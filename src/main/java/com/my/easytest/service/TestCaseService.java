package com.my.easytest.service;

import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.testcase.QueryTestCaseListDto;
import com.my.easytest.entity.TestCase;

public interface TestCaseService {

	/**
	 *  新增测试用例
	 * @param testCase
	 * @return
	 */
	ResultDto save(TestCase testCase);

	/**
	 *  删除测试用例信息
	 * @param caseId
	 * @param createUserId
	 * @return
	 */
	ResultDto<TestCase> delete(Integer caseId, Integer createUserId);

	/**
	 *  修改测试用例信息
	 * @param testCase
	 * @return
	 */
	ResultDto<TestCase> update(TestCase testCase);

	/**
	 *  根据id查询测试用例
	 * @param jenkinsId
	 * @param createUserId
	 * @return
	 */
	ResultDto<TestCase> getById(Integer caseId, Integer createUserId);

	/**
	 *  查询Jenkins信息列表
	 * @param pageTableRequest
	 * @return
	 */
	ResultDto<PageTableResponse<TestCase>> list(PageTableRequest<QueryTestCaseListDto> pageTableRequest);

	/**
	 *  根据用户id和caseId查询case原始数据-直接返回字符串，因为会保存为文件
	 * @param createUserId
	 * @param caseId
	 * @return
	 */
	String getCaseDataById(Integer createUserId, Integer caseId);

}
