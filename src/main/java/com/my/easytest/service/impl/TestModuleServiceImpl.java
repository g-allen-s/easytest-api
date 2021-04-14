package com.my.easytest.service.impl;

import com.my.easytest.common.TokenDb;
import com.my.easytest.dao.TestModuleMapper;
import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.dto.module.QueTestModuleListDto;
import com.my.easytest.entity.TestModule;
import com.my.easytest.service.TestModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TestModuleServiceImpl implements TestModuleService {

	@Autowired
	private TestModuleMapper testModuleMapper;

	@Autowired
	private TokenDb tokenDb;


	/**
	 * 新增模块信息
	 *
	 * @param testModule
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestModule> save(TestModule testModule) {

		TestModule qureyTestModule = new TestModule();
		qureyTestModule.setModuleName(testModule.getModuleName());
		TestModule storedTestModule = testModuleMapper.selectOne(qureyTestModule);

		if(Objects.nonNull(storedTestModule)){
			return ResultDto.fail("模块名已存在");
		}

		testModule.setCreateTime(new Date());
		testModule.setUpdateTime(new Date());
		testModuleMapper.insertUseGeneratedKeys(testModule);

		return ResultDto.success("成功", testModule);
	}

	/**
	 * 修改模块信息
	 *
	 * @param testModule
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestModule> update(TestModule testModule) {

		TestModule qureyTestModule = new TestModule();
		qureyTestModule.setId(testModule.getId());
		TestModule storedTestModule = testModuleMapper.selectOne(qureyTestModule);
		if(Objects.isNull(storedTestModule)){
			return ResultDto.fail("未查到模块信息");
		}

		TestModule qureyNameTakenTestModule = new TestModule();
		qureyNameTakenTestModule.setModuleName(testModule.getModuleName());
		TestModule nameTakenTestModule = testModuleMapper.selectOne(qureyNameTakenTestModule);
		if(Objects.nonNull(nameTakenTestModule) && testModule.getId() != nameTakenTestModule.getId()){
			return ResultDto.fail("模块名已存在");
		}

		testModule.setCreateTime(storedTestModule.getCreateTime());
		testModule.setUpdateTime(new Date());

		testModuleMapper.updateByPrimaryKey(testModule);

		return ResultDto.success("成功");
	}

	/**
	 * 根据ModuleId删除模块
	 *
	 * @param moduleId
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestModule> delete(Integer moduleId, TokenDto tokenDto) {

		TestModule qureyTestModule = new TestModule();
		qureyTestModule.setId(moduleId);
		//qureyTestModule.setCreateUserId(tokenDto.getUserId());
		TestModule storedTestModule = testModuleMapper.selectOne(qureyTestModule);
		if(Objects.isNull(storedTestModule)){
			return ResultDto.fail("未查到模块信息");
		}

		testModuleMapper.deleteByPrimaryKey(moduleId);

		return ResultDto.success("成功");
	}

	/**
	 * 查询模块信息列表
	 *
	 * @param pageTableRequest
	 * @return
	 */
	@Override
	public ResultDto<PageTableResponse<TestModule>> list(PageTableRequest<QueTestModuleListDto> pageTableRequest) {

		QueTestModuleListDto params = pageTableRequest.getParams();
		Integer pageNum = pageTableRequest.getPageNum();
		Integer pageSize = pageTableRequest.getPageSize();


		//总数
		Integer recordsTotal = testModuleMapper.count(params);

		//分页查询数据
		List<TestModule> testModules = testModuleMapper.list(params, (pageNum - 1) * pageSize, pageSize);

		PageTableResponse<TestModule> pageTableResponse = new PageTableResponse<>();
		pageTableResponse.setRecordsTotal(recordsTotal);
		pageTableResponse.setData(testModules);

		return ResultDto.success("成功", pageTableResponse);
	}

	/**
	 * 根据id查询模块信息
	 *
	 * @param moduleId
	 * @return createUserId
	 */
	@Override
	public ResultDto<TestModule> getById(Integer moduleId, Integer createUserId) {

		TestModule qureyTestModule = new TestModule();
		qureyTestModule.setId(moduleId);
		//qureyTestModule.setCreateUserId(tokenDto.getUserId());
		TestModule storedTestModule = testModuleMapper.selectOne(qureyTestModule);
		if(Objects.isNull(storedTestModule)){
			return ResultDto.fail("未查到模块信息");
		}

		return ResultDto.success("成功", storedTestModule);
	}
}
