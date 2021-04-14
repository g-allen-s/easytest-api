package com.my.easytest.service;

import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.dto.jenkins.QueryTestJenkinsListDto;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.entity.TestJenkins;

public interface TestJenkinsService {


	/**
	 *  新增Jenkins信息
	 * @param testJenkins
	 * @return
	 */
	ResultDto<TestJenkins> save(TokenDto tokenDto, TestJenkins testJenkins);

	/**
	 *  修改Jenkins信息
	 * @param testJenkins
	 * @return
	 */
	ResultDto<TestJenkins> update(TokenDto tokenDto, TestJenkins testJenkins);

	/**
	 *  删除Jenkins信息
	 * @param jenkinsId
	 * @return
	 */
	ResultDto<TestJenkins> delete(Integer jenkinsId, TokenDto tokenDto);

	/**
	 *  根据id查询Jenkins信息
	 * @param jenkinsId
	 * @return
	 */
	ResultDto<TestJenkins> getById(Integer jenkinsId, Integer createUserId);

	/**
	 *  查询Jenkins信息列表
	 * @param pageTableRequest
	 * @return
	 */
	ResultDto<PageTableResponse<TestJenkins>> list(PageTableRequest<QueryTestJenkinsListDto> pageTableRequest);

}
