package com.my.easytest.service;

import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.dto.module.QueTestModuleListDto;
import com.my.easytest.entity.TestModule;

public interface TestModuleService {


	/**
	 *  新增模块信息
	 * @param testModule
	 * @return
	 */
	ResultDto<TestModule> save(TestModule testModule);

	/**
	 *  更新模块信息
	 * @param testModule
	 * @return
	 */
	ResultDto<TestModule> update(TestModule testModule);

	/**
	 *  根据ModuleId删除模块
	 * @param moduleId
	 * @return
	 */
	ResultDto<TestModule> delete(Integer moduleId, TokenDto tokenDto);

	/**
	 *  查询列表信息
	 * @param pageTableRequest
	 * @return
	 */
	ResultDto<PageTableResponse<TestModule>> list(PageTableRequest<QueTestModuleListDto> pageTableRequest);

	/**
	 *  根据id查询模块信息
	 * @param moduleId
	 * @return
	 */
	ResultDto<TestModule> getById(Integer moduleId, Integer createUserId);

}
