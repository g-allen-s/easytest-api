package com.my.easytest.service.impl;

import com.my.easytest.common.TokenDb;
import com.my.easytest.dao.TestJenkinsMapper;
import com.my.easytest.dao.TestUserMapper;
import com.my.easytest.dto.PageTableRequest;
import com.my.easytest.dto.PageTableResponse;
import com.my.easytest.dto.TokenDto;
import com.my.easytest.dto.jenkins.QueryTestJenkinsListDto;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.entity.TestJenkins;
import com.my.easytest.entity.TestUser;
import com.my.easytest.service.TestJenkinsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TestJenkinsServiceImpl implements TestJenkinsService {

	@Autowired
	private TestJenkinsMapper testJenkinsMapper;

	@Autowired
	private TestUserMapper testUserMapper;

	@Autowired
	private TokenDb tokenDb;


	/**
	 * 新增Jenkins信息
	 *
	 * @param testJenkins
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestJenkins> save(TokenDto tokenDto, TestJenkins testJenkins) {

		testJenkins.setCreateTime(new Date());
		testJenkins.setUpdateTime(new Date());
		testJenkinsMapper.insertUseGeneratedKeys(testJenkins);

		Integer defaultJenkinsFlag = testJenkins.getDefaultFlag();
		if(Objects.nonNull(defaultJenkinsFlag) && defaultJenkinsFlag == 1){
			//更新user表中的默认JenkinsId
			TestUser testUser = new TestUser();
			testUser.setId(testJenkins.getCreateUserId());
			testUser.setDefaultJenkinsId(testJenkins.getId());
			testUserMapper.updateByPrimaryKeySelective(testUser);
			//更新token信息中的默认JenkinsId
			tokenDto.setDefaultJenkinsId(testJenkins.getId());
			tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);
		}

		return ResultDto.success("成功", testJenkins);
	}

	/**
	 * 修改Jenkins信息
	 *
	 * @param testJenkins
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestJenkins> update(TokenDto tokenDto, TestJenkins testJenkins) {

		TestJenkins queryTestJenkins = new TestJenkins();
		queryTestJenkins.setId(testJenkins.getId());
		queryTestJenkins.setCreateUserId(testJenkins.getCreateUserId());
		TestJenkins result = testJenkinsMapper.selectOne(queryTestJenkins);

		//如果为空，则提示，也可以直接返回成功
		if(Objects.isNull(result)){
			return ResultDto.fail("未查到Jenkins信息");
		}

		testJenkins.setCreateTime(result.getCreateTime());
		testJenkins.setUpdateTime(new Date());
		testJenkinsMapper.updateByPrimaryKey(testJenkins);

		Integer defaultJenkinsFlag = testJenkins.getDefaultFlag();
		if(Objects.nonNull(defaultJenkinsFlag) && defaultJenkinsFlag==1){
            //更新user表中的默认JenkinsId
			TestUser testUser = new TestUser();
			testUser.setId(testJenkins.getCreateUserId());
			testUser.setDefaultJenkinsId(testJenkins.getId());
			testUserMapper.updateByPrimaryKeySelective(testUser);
			//更新token信息中的默认JenkinsId
			tokenDto.setDefaultJenkinsId(testJenkins.getId());
			tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);
		}

		if(Objects.nonNull(defaultJenkinsFlag) && defaultJenkinsFlag==0){
			TestUser queryTestUser = new TestUser();
			queryTestUser.setId(tokenDto.getUserId());
			queryTestUser.setDefaultJenkinsId(testJenkins.getId());
			TestUser reslutTestUser = testUserMapper.selectOne(queryTestUser);

			if(Objects.nonNull(reslutTestUser)) {
				//更新user表中的默认JenkinsId
				TestUser testUser = new TestUser();
				testUser.setId(tokenDto.getUserId());
				testUser.setDefaultJenkinsId(null);
//				testUserMapper.updateDefaultJenkinsId(testUser);
				//更新token信息中的默认JenkinsId
				tokenDto.setDefaultJenkinsId(null);
				tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);
			}
		}

		return ResultDto.success("成功");
	}

	/**
	 * 删除Jenkins信息
	 *
	 * @param jenkinsId
	 * @return createUserId
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<TestJenkins> delete(Integer jenkinsId, TokenDto tokenDto) {

		TestJenkins queryTestJenkins = new TestJenkins();
		queryTestJenkins.setId(jenkinsId);
		queryTestJenkins.setCreateUserId(tokenDto.getUserId());
		TestJenkins result = testJenkinsMapper.selectOne(queryTestJenkins);

		//如果为空，则提示，也可以直接返回成功
		if(Objects.isNull(result)){
			return ResultDto.fail("未查到Jenkins信息");
		}

		TestUser queryTestUser = new TestUser();
		queryTestUser.setId(tokenDto.getUserId());
		TestUser reslutTestUser = testUserMapper.selectOne(queryTestUser);

		Integer defaultJenkinsId = reslutTestUser.getDefaultJenkinsId();
		if(Objects.nonNull(defaultJenkinsId) && defaultJenkinsId.equals(jenkinsId)){
			//更新user表中的默认JenkinsId
			TestUser testUser = new TestUser();
			testUser.setId(tokenDto.getUserId());
			testUser.setDefaultJenkinsId(null);
//			testUserMapper.updateDefaultJenkinsId(testUser);
			//更新token信息中的默认JenkinsId
			tokenDto.setDefaultJenkinsId(null);
			tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);
		}

		testJenkinsMapper.deleteByPrimaryKey(jenkinsId);

		return ResultDto.success("删除成功");
	}

	/**
	 * 根据id查询Jenkins信息
	 *
	 * @param jenkinsId
	 * @return createUserId
	 */
	@Override
	public ResultDto<TestJenkins> getById(Integer jenkinsId, Integer createUserId) {

		TestJenkins queryTestJenkins = new TestJenkins();
		queryTestJenkins.setId(jenkinsId);
		queryTestJenkins.setCreateUserId(createUserId);
		TestJenkins result = testJenkinsMapper.selectOne(queryTestJenkins);

		//如果为空，则提示，也可以直接返回成功
		if(Objects.isNull(result)){
			ResultDto.fail("未查到Jenkins信息");
		}

		TestUser queryTestUser = new TestUser();
		queryTestUser.setId(createUserId);
		TestUser resultTestUser = testUserMapper.selectOne(queryTestUser);

		Integer defaultJenkinsId = resultTestUser.getDefaultJenkinsId();
		if(result.getId().equals(defaultJenkinsId)){
			result.setDefaultFlag(1);
		}

		return ResultDto.success("成功", result);
	}

	/**
	 * 查询Jenkins信息列表
	 *
	 * @param pageTableRequest
	 * @return
	 */
	@Override
	public ResultDto<PageTableResponse<TestJenkins>> list(PageTableRequest<QueryTestJenkinsListDto> pageTableRequest) {

		QueryTestJenkinsListDto params = pageTableRequest.getParams();
		Integer pageNum = pageTableRequest.getPageNum();
		Integer pageSize = pageTableRequest.getPageSize();
		Integer createUserId = params.getCreateUserId();

		TestUser queryTestUser = new TestUser();
		queryTestUser.setId(createUserId);
		TestUser resultTestUser = testUserMapper.selectOne(queryTestUser);

		Integer defaultJenkinsId = resultTestUser.getDefaultJenkinsId();

		//总数
		Integer recordsTotal =  testJenkinsMapper.count(params);

		//分页查询数据
		List<TestJenkins> testJenkinsList = testJenkinsMapper.list(params, (pageNum - 1) * pageSize, pageSize);

		//查找默认Jenkins
		for (TestJenkins testJenkins : testJenkinsList) {
			if(testJenkins.getId().equals(defaultJenkinsId)){
				testJenkins.setDefaultFlag(1);
			}
		}

		PageTableResponse<TestJenkins> testJenkinsPageTableResponse = new PageTableResponse<>();
		testJenkinsPageTableResponse.setRecordsTotal(recordsTotal);
		testJenkinsPageTableResponse.setData(testJenkinsList);

		return ResultDto.success("成功", testJenkinsPageTableResponse);
	}
}
